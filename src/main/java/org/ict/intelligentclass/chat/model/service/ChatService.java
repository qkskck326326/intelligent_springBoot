package org.ict.intelligentclass.chat.model.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.MessageReadCompositeKey;
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

    /**
     * 안 읽은 메시지 갯수 확인
     * 1. chatUserRepository.findRoomIdsByUserId(userId) => O(1)
     * 2. messageReadRepository.countByRoomIdAndUserId(chatroomId, userId) -> O(1)
     * 2의 for 반복문 => O(I) I: 채팅방 모든 아이디들
     * 최종 시간 복잡도 => O(I) => O(n)
     * */
    public Long countTotalUnread(String userId) {

        List<Long> chatroomIds = chatUserRepository.findRoomIdsByUserId(userId);
        if(chatroomIds == null || chatroomIds.isEmpty()) {
            return 0L;
        } else {
            long sum = 0;
            for (Long chatroomId : chatroomIds) {
                Long totalMessages = chatMessageRepository.countByRoomId(chatroomId);
                Long readMessages = messageReadRepository.countByRoomIdAndUserId(chatroomId, userId);
                Long unreadMessageCount = totalMessages - readMessages;
                sum += unreadMessageCount;
            }
            return sum;
        }
    }

    /**
     * 채팅방 생성
     * 1. ChatroomEntity newRoom = chatroomRepository.save(chatroomEntity) -> O(1)
     * 2. chatUserRepository.save(chatUserEntity); -> O(1)
     * 2의 for 반복문 -> O(U) U: 추가된 인원수
     * 최종 시간 복잡도 O(U) => O(n)
     * */
    public ChatroomEntity insertRoom(List<String> people, String roomType) {
        String roomName = (roomType.equals("inquiries")) ? "문의" : String.join(", ", people);
        String creator = people.get(0);
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

    /**
     * 채팅방 리스트 가져오기
     * 1. chatUserRepository.findRoomIdsByUserIdOrderByIsPinned(userId) => O(R) R: 채팅방 일련번호
     * -> 작동방식 : userId를 통해서 해당하는 방을 찾기위해 모든 채팅방을 확인
     * 2. chatroomRepository.findChatsByRoomIds(roomIds) => O(R)
     * 3. for 반복문 안 메소드들 => O(1)
     * 4. for 반복문 O(U) U: 채팅방내 유저
     * 최종 시간 복잡도 O(2R x U) => O(n^2) => 최적화 필요
     *
     * **개선할만한 점***
     * => Lazy Loading : 바로 모든 데이터 fetch하지말고 페이지네이션 적용 그리고 유저 스크롤로 추가 데이터 가져오도록 수정
     * => 캐싱 : 자주 접근하는 데이터를 캐싱하는 메커니즘 적용
     * => 자주 접근해야하는 데이터를 엔티티에 추가: 가장 마지막으로 전송된 메시지칼럼을 chatroom엔티티에 추가
     *    메시지 전송 메커니즘에 chatroom에서 마지막으로 전송된 메시지 아이디 변경로직도 추가
     * => 필요한 데이터만 가져오는 Dto 만들기 : 유저엔티티를 전부 가져가지 말고 필요한 칼럼만 추려서 가져가기
     * */
    public List<ChatroomDetailsDto> getChatrooms(String userId, boolean isChats) {
        List<Long> roomIds = chatUserRepository.findRoomIdsByUserIdOrderByIsPinned(userId);
        List<ChatroomEntity> chatrooms;
        if (isChats) {
            chatrooms = chatroomRepository.findChatsByRoomIds(roomIds);
        } else {
            chatrooms = chatroomRepository.findInquiriesByRoomIds(roomIds);
        }
        List<ChatroomDetailsDto> chatroomDetails = new ArrayList<>();
        for (ChatroomEntity chatroom : chatrooms) {
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
                    if (pinnedComparison != 0) {return pinnedComparison;}
                    if (c1.getLatestMessageTimestamp() == null && c2.getLatestMessageTimestamp() == null) {return 0;}
                    if (c1.getLatestMessageTimestamp() == null) {return 1;}
                    if (c2.getLatestMessageTimestamp() == null) {return -1;}
                    return c2.getLatestMessageTimestamp().compareTo(c1.getLatestMessageTimestamp());
                })
                .collect(Collectors.toList());
    }

    /**
     * 채팅방 내부 메세지들 가져오기
     * 1. chatMessageRepository.findFirstByRoomIdAndIsAnnouncement(roomId, 1L) : O(1)
     * 2. chatMessageRepository.findByRoomId(roomId, PageRequest.of(page - 1, 25, Sort.by("dateSent").descending())) : O(M) M: 메시지 갯수
     * 3. for 반복문 -> O(M)
     * 4. 3내부의 메소드들 -> O(1)
     * 최종 시간 복잡도 O(2M) => O(n)
     * */
    public ChatMessagesResponse getMessages(String userId, Long roomId, int page) {
        Optional<ChatMessageEntity> announcementOpt = chatMessageRepository.findFirstByRoomIdAndIsAnnouncement(roomId, 1L);
        ChatMessageDto announcementDto = null;
        if (announcementOpt.isPresent()) {
            announcementDto = convertToDto(announcementOpt.get(), userId, roomId);
        }
        List<ChatMessageEntity> messages = chatMessageRepository.findByRoomId(roomId, PageRequest.of(page - 1, 25, Sort.by("dateSent").descending()));
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
        if (messageDtos.isEmpty() && announcementDto == null) {
            return new ChatMessagesResponse(null, List.of());
        }
        return new ChatMessagesResponse(announcementDto, messageDtos);
    }

    /**
     * 채팅읽음 처리
     * 1. messageReadRepository.existsByMessageIdAndUserId(message.getMessageId(), userId) => O(1)
     * 2. messageReadRepository.save(newRead) => O(1)
     * 최종 시간 복잡도 O(1)
     * */
    private void markMessageAsRead(String userId, Long roomId, ChatMessageEntity message) {
        MessageReadCompositeKey compositeKey = new MessageReadCompositeKey(message.getMessageId(), userId);
        boolean isRead = messageReadRepository.existsByMessageReadCompositeKey(compositeKey);
        if (!isRead) {
            MessageReadEntity newRead = new MessageReadEntity();
            newRead.setMessageReadCompositeKey(compositeKey);
            newRead.setRoomId(roomId);
            newRead.setReadAt(new Date());
            messageReadRepository.save(newRead);
        }
    }

//    public MessageReadEntity markRead(MessageReadCompositeKey compositeKey, Long roomId) {
//
//        MessageReadEntity newRead = new MessageReadEntity();
//        newRead.setMessageReadCompositeKey(compositeKey);
//        newRead.setRoomId(roomId);
//        newRead.setReadAt(new Date());
//        return messageReadRepository.save(newRead);
//    }


    /**
     * Dto로 변환
     * 모든 메소드들 : O(1)
     * 최종 시간 복잡도 => O(1)
     * */
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
        int readCount = (int) messageReadRepository.countByMessageReadCompositeKeyMessageId(message.getMessageId());
        dto.setReadCount(readCount);

        // Check if current user has read the message
        MessageReadCompositeKey compositeKey = new MessageReadCompositeKey(message.getMessageId(), userId);
        boolean isReadByCurrentUser = messageReadRepository.existsByMessageReadCompositeKey(compositeKey);
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

    /**
     * 복합키로 유저 엔티티 반환
     * 최종 시간 복잡도 O(1)
     * */
    public ChatUserEntity getChatUserDetail(String userId, Long roomId) {
        return chatUserRepository.findByChatUserCompositeKeyUserIdAndChatUserCompositeKeyRoomId(userId, roomId);
    }

    /**
     * 채팅방 핀 설정/제거
     * 1. chatUserRepository.findById(key) => O(1)
     * 2. chatUserRepository.save(chatUser) => O(1)
     * 최종 시간 복잡도 O(1)
     * */
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
    /**
     * 채팅방 나가기
     * 1. chatUserRepository.existsById(key): O(1)
     * 2. chatUserRepository.deleteById(key): O(1)
     *  시간 복잡도 (일반적인 경우) => O(1)
     * 3. 만일 나가는 사람이 마지막이라 내부 파일, 메시지, 최종적으로 방까지 삭제
     * 파일 삭제용 for 반복문 : O(F) F: 채팅방 내 파일 갯수
     * 최종 시간 복잡도 O(1) + O(F) => O(n)
     * */
    public void leaveRoom(String userId, Long roomId) {

        ChatUserCompositeKey key = new ChatUserCompositeKey(userId, roomId);

        if (chatUserRepository.existsById(key)) {
            chatUserRepository.deleteById(key);

            //추가적으로 방에 아무도 안 남아있으면
            //관련 정보가 모두 지워짐
            if (chatUserRepository.countByRoomId(roomId) == 0) {
                messageReadRepository.deleteByRoomId(roomId);

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
                chatMessageRepository.deleteByRoomId(roomId);
                chatroomRepository.deleteById(roomId);
            }

        } else {
            throw new EntityNotFoundException("그런 유저 아리마셍");
        }
    }
    /**
     * 채팅방 이름 변경
     * 1. chatroomRepository.findById(roomId): O(1)
     * 2. chatroomRepository.save(chatRoom): O(1)
     * 최종 시간 복잡도 : O(1)
     * */
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
    /**
     * 채팅방 내부 공지사항 변경
     * 1. chatMessageRepository.findAnnouncementByRoomId(roomId):  O(1)
     * 2. chatMessageRepository.save(currentAnnouncement): O(1)
     * 3. chatMessageRepository.findById(messageId): O(1)
     * 4. chatMessageRepository.save(newAnnouncement):  O(1)
     * 최종 시간 복잡도 : O(1)
     * */
    public ChatMessageEntity updateAnnouncement(Long roomId, Long messageId) {
        ChatMessageEntity currentAnnouncement = chatMessageRepository.findAnnouncementByRoomId(roomId);
        if (currentAnnouncement != null) {
            currentAnnouncement.setIsAnnouncement(0L);
            chatMessageRepository.save(currentAnnouncement);
        }
        Optional<ChatMessageEntity> newAnnouncementOptional = chatMessageRepository.findById(messageId);
        ChatMessageEntity newAnnouncement = newAnnouncementOptional.orElseThrow(() -> new NoSuchElementException("Message not found"));
        newAnnouncement.setIsAnnouncement(1L);
        return chatMessageRepository.save(newAnnouncement);
    }

    /**
     * 메시지 저장
     * 모든 메소드들 : O(1)
     * 최종 시간 복잡도 : O(1)
     * */
    public ChatMessageEntity saveMessage(ChatMessageEntity chatMessageEntity) {
        chatMessageEntity.setDateSent(new Date());
        ChatMessageEntity savedMessage = chatMessageRepository.save(chatMessageEntity);
        MessageReadCompositeKey compositeKey = new MessageReadCompositeKey(savedMessage.getMessageId(), chatMessageEntity.getSenderId());
        MessageReadEntity messageReadEntity = new MessageReadEntity();
        messageReadEntity.setMessageReadCompositeKey(compositeKey);
        messageReadEntity.setRoomId(chatMessageEntity.getRoomId());
        messageReadEntity.setReadAt(new Date());
        messageReadRepository.save(messageReadEntity);
        return savedMessage;
    }

    /**
     * 파일 저장
     * 1. for 반복문 : O(F) : 첨부된 파일 갯수
     * 최종 시간 복잡도 : O(F) => O(n)
     * */
    public List<MessageFileEntity> saveFiles(ChatMessageEntity message, List<MultipartFile> files) {
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

    /**
     * 메시지 삭제
     * 1. chatMessageRepository.findById(messageId): O(1)
     * 2. for 반복문 messageFiles: O(F) F: 파일 갯수
     * 3. 반복문 내 메소드들: O(1)
     * 4. messageFileRepository.deleteByMessageId(messageId): O(1)
     * 5. chatMessageRepository.save(chatMessage): O(1)
     * 최종 시간 복잡도 : O(F) => O(n)
     * */
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

    /**
     * 채팅방 일련번호로 방 내부 유저들 엔티티 반환
     * 1. chatUserRepository.findUserIdsByRoomId(roomId): O(U)
     * 2. 가져온 유저닉네임으로 for 반복문 : O(U)
     * 3. userRepository.findByNickname(userNickname): O(1)
     * 최종 시간 복잡도 : O(2U) => O(n)
     * */
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

    /**
     * Dto로 변환
     * 모든 메소드들 : O(1)
     * 최종 시간 복잡도 => O(1)
     * */
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

