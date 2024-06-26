package org.ict.intelligentclass.chat.model.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.controller.ChatWebSocketController;
import org.ict.intelligentclass.chat.jpa.entity.*;
import org.ict.intelligentclass.chat.jpa.repository.*;
import org.ict.intelligentclass.chat.model.dto.ChatMessageDto;
import org.ict.intelligentclass.chat.model.dto.ChatroomDetailsDto;
import org.ict.intelligentclass.chat.model.dto.MessageFileDto;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.ict.intelligentclass.chat.model.dto.ChatMessagesResponse;
import org.springframework.data.domain.Sort;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatUserRepository chatUserRepository;
    private final MessageFileRepository messageFileRepository;
    private final MessageReadRepository messageReadRepository;
    private final UserRepository userRepository;


    public Long selectRoomIds(String userId) {

        log.info("selectRoomIds userId = " + userId);
        //챗유저 닉네임으로 모든 채팅방 아이디 가져오기
        List<Long> chatroomIds = chatUserRepository.findRoomIdsByUserId(userId);

        log.info("선택된 챗방 아이디들: {}", chatroomIds);

        //만약 아무것도 없다면 리턴
        if(chatroomIds == null || chatroomIds.isEmpty()) {
            return 0L;

        } else {
            //있을 경우 하나씩 꺼내서 채팅방 확인
            long sum = 0;
            for (Long chatroomId : chatroomIds) {

                //전체 메시지 갯수
                long eachTotalMessageCount = chatMessageRepository.countByRoomId(chatroomId);

                //읽음 메시지 확인
                long eachTotalReadMessageCount = messageReadRepository.countByRoomId(chatroomId);

                long UnreadMessageCount = eachTotalMessageCount - eachTotalReadMessageCount;
                sum += UnreadMessageCount;
            }
            return sum;
        }
    }

    public ChatroomEntity insertRoom(List<String> people, String roomType) {
        log.info("insertRoom people = " + people);

        String roomName = (roomType.equals("inquiries")) ? "문의" : String.join(", ", people);
        String creator = people.get(0); // Assuming the first person in the list is the creator

        ChatroomEntity chatroomEntity = new ChatroomEntity();
        chatroomEntity.setRoomName(roomName);
        switch (roomType) {
            case "groups":
                chatroomEntity.setRoomType("group");
                break;
            case "inquiries":
                chatroomEntity.setRoomType("inquiries");
                break;
            default:
                chatroomEntity.setRoomType("individual");
                break;
        }
        chatroomEntity.setCreatedAt(new Date());
        chatroomEntity.setCreator(creator);

        ChatroomEntity newRoom = chatroomRepository.save(chatroomEntity);
        log.info(newRoom.toString());

        Long roomId = newRoom.getRoomId();

        for (String userId : people) {
            ChatUserCompositeKey key = new ChatUserCompositeKey(userId, roomId);
            ChatUserEntity chatUserEntity = ChatUserEntity.builder()
                    .chatUserCompositeKey(key)
                    .isMuted(0L)
                    .isPinned(0L)
                    .build();

            chatUserRepository.save(chatUserEntity);
        }

        return newRoom;
    }


    public List<ChatroomDetailsDto> getChatrooms(String userId) {

        //유저아이디로 채팅방아이디 모두 가져옴
        List<Long> roomIds = chatUserRepository.findRoomIdsByUserIdOrderByIsPinned(userId);
        //아이디로 방 정보 가져옴
        List<ChatroomEntity> chatrooms = chatroomRepository.findByRoomIdIn(roomIds);

        List<ChatroomDetailsDto> chatroomDetails = new ArrayList<>();

        for (ChatroomEntity chatroom : chatrooms) {
            //방정보 하나씩 돌려서 정보 가져옴
            ChatUserEntity chatUser = chatUserRepository.findByChatUserCompositeKeyUserIdAndChatUserCompositeKeyRoomId(userId, chatroom.getRoomId());
            ChatMessageEntity latestMessage = chatMessageRepository.findTopByRoomIdOrderByDateSentDesc(chatroom.getRoomId());
            Date latestMessageTimestamp = latestMessage != null ? latestMessage.getDateSent() : null;
            int totalPeople = chatUserRepository.countByRoomId(chatroom.getRoomId());
            List<String> userNicknames = chatUserRepository.findUserIdsByRoomId(chatroom.getRoomId());
            List<UserEntity> users = new ArrayList<>();
            for(String userNickname : userNicknames) {
                Optional<UserEntity> user = userRepository.findByNickname(userNickname);
                if(user.isPresent()) {
                    UserEntity userEntity = user.get();
                    users.add(userEntity);
                }
            }
            Long totalMessages = chatMessageRepository.countByRoomId(chatroom.getRoomId());
            Long readMessages = messageReadRepository.countByRoomIdAndUserId(chatroom.getRoomId(), userId);
            Long unreadMessageCount = totalMessages - readMessages;

            chatroomDetails.add(new ChatroomDetailsDto(chatroom, chatUser, latestMessage, totalPeople, latestMessageTimestamp, unreadMessageCount, users));
        }

        return chatroomDetails.stream()
                .sorted((c1, c2) -> {
                    int pinnedComparison = c2.getChatUser().getIsPinned().compareTo(c1.getChatUser().getIsPinned());
                    if (pinnedComparison != 0) {
                        return pinnedComparison;
                    }
                    if (c1.getLatestMessageTimestamp() == null && c2.getLatestMessageTimestamp() == null) {
                        return 0;
                    }
                    if (c1.getLatestMessageTimestamp() == null) {
                        return 1;
                    }
                    if (c2.getLatestMessageTimestamp() == null) {
                        return -1;
                    }
                    return c2.getLatestMessageTimestamp().compareTo(c1.getLatestMessageTimestamp());
                })
                .collect(Collectors.toList());
    }

    public ChatMessagesResponse getMessages(String userId, Long roomId, int page) {
        // Fetch announcement for the room if exists
        Optional<ChatMessageEntity> announcementOpt = chatMessageRepository.findFirstByRoomIdAndIsAnnouncement(roomId, 1L);
        ChatMessageDto announcementDto = null;
        if (announcementOpt.isPresent()) {
            announcementDto = convertToDto(announcementOpt.get(), userId, roomId);
        }

        // Fetch the messages for the room, paginated
        List<ChatMessageEntity> messages = chatMessageRepository.findByRoomId(roomId, PageRequest.of(page - 1, 25, Sort.by("dateSent").descending()));

        // Convert messages to DTOs and fetch associated files if needed
        List<ChatMessageDto> messageDtos = messages.stream()
                .map(message -> {
                    markMessageAsRead(userId, roomId, message);
                    ChatMessageDto messageDto = convertToDto(message, userId, roomId);

                    if (message.getMessageType() == 1 || message.getMessageType() == 2 || message.getMessageType() == 3) {
                        List<MessageFileEntity> files = messageFileRepository.findByMessageId(message.getMessageId());
                        List<MessageFileDto> fileDtos = files.stream()
                                .map(file -> MessageFileDto.builder()
                                        .fileId(file.getFileId())
                                        .messageId(file.getMessageId())
                                        .senderId(file.getSenderId())
                                        .roomId(file.getRoomId())
                                        .fileURL(file.getFileURL())
                                        .fileSize(file.getFileSize())
                                        .originalName(file.getOriginalName())
                                        .renamedName(file.getRenamedName())
                                        .build())
                                .collect(Collectors.toList());
                        messageDto.setFiles(fileDtos);
                    }

                    return messageDto;
                })
                .collect(Collectors.toList());

        // Handle no messages scenario
        if (messageDtos.isEmpty() && announcementDto == null) {
            return new ChatMessagesResponse(null, List.of());
        }

        return new ChatMessagesResponse(announcementDto, messageDtos);
    }

    private void markMessageAsRead(String userId, Long roomId, ChatMessageEntity message) {
        boolean isRead = messageReadRepository.existsByMessageIdAndUserId(message.getMessageId(), userId);
        if (!isRead) {
            MessageReadEntity newRead = new MessageReadEntity();
            newRead.setMessageId(message.getMessageId());
            newRead.setUserId(userId);
            newRead.setRoomId(roomId);
            newRead.setReadAt(new Date());
            messageReadRepository.save(newRead);
        }
    }

    private ChatMessageDto convertToDto(ChatMessageEntity message, String userId, Long roomId) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setMessageId(message.getMessageId());
        dto.setRoomId(message.getRoomId());
        dto.setSenderId(message.getSenderId());
        dto.setMessageContent(message.getMessageContent());
        dto.setMessageType(message.getMessageType());
        dto.setDateSent(message.getDateSent());
        dto.setAnnouncement(message.getIsAnnouncement() == 1);

        // Check read count
        int readCount = messageReadRepository.countByMessageId(message.getMessageId());
        dto.setReadCount(readCount);

        // Check if current user has read the message
        boolean isReadByCurrentUser = messageReadRepository.existsByMessageIdAndUserId(message.getMessageId(), userId);
        dto.setReadByCurrentUser(isReadByCurrentUser);

        // Fetch sender information
        Optional<UserEntity> senderOptional = userRepository.findByNickname(message.getSenderId());
        if (senderOptional.isPresent()) {
            UserEntity sender = senderOptional.get();
            dto.setSenderEmail(sender.getUserId().getUserEmail());
            dto.setSenderProfileImageUrl(sender.getProfileImageUrl());
            dto.setUserType(sender.getUserType());
        }

        return dto;
    }

    public ChatUserEntity getChatUserDetail(String userId, Long roomId) {
        return chatUserRepository.findByChatUserCompositeKeyUserIdAndChatUserCompositeKeyRoomId(userId, roomId);
    }

    public ChatUserEntity changePinStatus(String userId, Long roomId, Long isPinned) {
        ChatUserCompositeKey key = new ChatUserCompositeKey(userId, roomId);
        Optional<ChatUserEntity> optionalChatUser = chatUserRepository.findById(key);

        if (optionalChatUser.isPresent()) {
            ChatUserEntity chatUser = optionalChatUser.get();
            chatUser.setIsPinned(isPinned);
            return chatUserRepository.save(chatUser);
        } else {
            throw new EntityNotFoundException("그런 유저 아리마셍");
        }
    }

    public void leaveRoom(String userId, Long roomId) {
        ChatUserCompositeKey key = new ChatUserCompositeKey(userId, roomId);
        if (chatUserRepository.existsById(key)) {
            log.info("여기 실행1");
            chatUserRepository.deleteById(key);

            //추가적으로 방에 아무도 안 남아있으면
            //관련 정보가 모두 지워짐
            if (chatUserRepository.countByRoomId(roomId) == 0) {
                log.info("여기 실행2");
                messageReadRepository.deleteByRoomId(roomId);
                log.info("파일 삭제 실행");

                List<MessageFileEntity> messageFiles = messageFileRepository.findByRoomId(roomId);
                for (MessageFileEntity fileEntity : messageFiles) {
                    String fileStorageLocation = "src/main/resources/static/uploads/";
                    File file = new File(fileStorageLocation + fileEntity.getRenamedName());
                    log.info(file.toString());
                    if (file.exists()) {
                        file.delete();
                    }
                }
                messageFileRepository.deleteByRoomId(roomId);
                log.info("여기 실행3");
                chatMessageRepository.deleteByRoomId(roomId);
                log.info("여기 실행4");
                chatroomRepository.deleteById(roomId);
                log.info("여기 실행5");
            }

        } else {
            throw new EntityNotFoundException("그런 유저 아리마셍");
        }
    }

    public ChatroomEntity changeRoomName(Long roomId, String roomName) {
        Optional<ChatroomEntity> optionalChatRoom = chatroomRepository.findById(roomId);

        if (optionalChatRoom.isPresent()) {
            ChatroomEntity chatRoom = optionalChatRoom.get();
            chatRoom.setRoomName(roomName);
            return chatroomRepository.save(chatRoom);
        } else {
            throw new EntityNotFoundException("그른 방 읍그든여");
        }
    }

    public ChatMessageEntity updateAnnouncement(Long roomId, Long messageId) {
        ChatMessageEntity currentAnnouncement = chatMessageRepository.findAnnouncementByRoomId(roomId);

        if (currentAnnouncement != null) {
            currentAnnouncement.setIsAnnouncement(0L);
            chatMessageRepository.save(currentAnnouncement);
        }

        // Set new announcement
        Optional<ChatMessageEntity> newAnnouncementOptional = chatMessageRepository.findById(messageId);
        ChatMessageEntity newAnnouncement = newAnnouncementOptional.orElseThrow(() -> new NoSuchElementException("Message not found"));
        newAnnouncement.setIsAnnouncement(1L);
        return chatMessageRepository.save(newAnnouncement);
    }

    public ChatMessageEntity saveMessage(ChatMessageEntity chatMessageEntity) {
        log.info("Saving message: {}", chatMessageEntity);
        chatMessageEntity.setDateSent(new Date());
        ChatMessageEntity savedMessage = chatMessageRepository.save(chatMessageEntity);
        log.info("Message saved: {}", savedMessage);

        // Mark message as read by sender
        MessageReadEntity messageReadEntity = new MessageReadEntity();
        messageReadEntity.setMessageId(savedMessage.getMessageId());
        messageReadEntity.setUserId(chatMessageEntity.getSenderId());
        messageReadEntity.setRoomId(chatMessageEntity.getRoomId());
        messageReadEntity.setReadAt(new Date());

        messageReadRepository.save(messageReadEntity);
        log.info("Message read entry saved: {}", messageReadEntity);

        return savedMessage;
    }

    public List<MessageFileEntity> saveFiles(ChatMessageEntity message, List<MultipartFile> files) {
        log.info("서비스 실행됨");

        List<MessageFileEntity> fileEntities = new ArrayList<>();
        for (MultipartFile file : files) {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = "";
            int lastIndexOfDot = originalFileName.lastIndexOf('.');
            if (lastIndexOfDot > 0) {
                fileExtension = originalFileName.substring(lastIndexOfDot);
            }
            String randomString = UUID.randomUUID().toString();
            String renamedFileName = randomString + "_" + System.currentTimeMillis() + fileExtension;
            log.info(renamedFileName);

            String fileStorageLocation = "src/main/resources/static/uploads";
            Path filePath = Paths.get(fileStorageLocation, renamedFileName);
            log.info(filePath.toString());
            try {
                Files.write(filePath, file.getBytes());

                MessageFileEntity fileEntity = new MessageFileEntity();
                fileEntity.setMessageId(message.getMessageId());
                fileEntity.setSenderId(message.getSenderId());
                fileEntity.setRoomId(message.getRoomId());
                fileEntity.setFileURL("/chat/files/" + renamedFileName);
                fileEntity.setFileSize(String.valueOf(file.getSize()));
                fileEntity.setOriginalName(originalFileName);
                fileEntity.setRenamedName(renamedFileName);

                fileEntities.add(messageFileRepository.save(fileEntity));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileEntities;
    }

    public ChatMessageEntity deleteMessage(Long messageId) {

        Optional<ChatMessageEntity> optionalChatMessage = chatMessageRepository.findById(messageId);

        if (!optionalChatMessage.isPresent()) {
            throw new EntityNotFoundException("Message not found");
        }

        ChatMessageEntity chatMessage = optionalChatMessage.get();

        List<MessageFileEntity> messageFiles = messageFileRepository.findByMessageId(messageId);
        if (!messageFiles.isEmpty()) {
            for (MessageFileEntity fileEntity : messageFiles) {
                String fileStorageLocation = "src/main/resources/static/uploads/";
                File file = new File(fileStorageLocation + fileEntity.getRenamedName());
                log.info(file.toString());
                if (file.exists()) {
                    file.delete();
                }
            }
        }

        messageFileRepository.deleteByMessageId(messageId);

        chatMessage.setMessageContent("삭제된 메시지입니다․");
        chatMessage.setMessageType(0L);

        return chatMessageRepository.save(chatMessage);
    }

    public List<UserEntity> getPeople(Long roomId) {

        List<String> userNicknames = chatUserRepository.findUserIdsByRoomId(roomId);
        List<UserEntity> users = new ArrayList<>();
        for(String userNickname : userNicknames) {
            Optional<UserEntity> user = userRepository.findByNickname(userNickname);
            if(user.isPresent()) {
                UserEntity userEntity = user.get();
                users.add(userEntity);
            }
        }
        return users;
    }


    public ChatMessageDto convertToDto(ChatMessageEntity message) {
        UserEntity sender = userRepository.findByNickname(message.getSenderId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<MessageFileEntity> files = messageFileRepository.findByMessageId(message.getMessageId());

        List<MessageFileDto> fileDtos = files.stream()
                .map(file -> new MessageFileDto(file.getFileId(), file.getMessageId(), file.getSenderId(), file.getRoomId(),
                        file.getFileURL(), file.getFileSize(), file.getOriginalName(), file.getRenamedName()))
                .collect(Collectors.toList());

        return ChatMessageDto.builder()
                .messageId(message.getMessageId())
                .roomId(message.getRoomId())
                .senderId(message.getSenderId())
                .roomId(message.getRoomId())
                .senderEmail(sender.getUserId().getUserEmail())
                .senderProfileImageUrl(sender.getProfileImageUrl())
                .messageContent(message.getMessageContent())
                .messageType(message.getMessageType())
                .dateSent(message.getDateSent())
                .isAnnouncement(message.getIsAnnouncement() == 1)
                .readCount(0) // Initialize with 0, update as needed
                .isReadByCurrentUser(false) // Initialize with false, update as needed
                .userType(sender.getUserType())
                .files(fileDtos)
                .build();
    }
}


//    public ArrayList<ChatUserEntity> selectChatrooms(String userId) {
//        log.info("selectChatrooms userId = " + userId);
//
//    }

