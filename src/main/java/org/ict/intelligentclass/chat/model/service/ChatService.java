package org.ict.intelligentclass.chat.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.*;
import org.ict.intelligentclass.chat.jpa.repository.*;
import org.ict.intelligentclass.chat.model.dto.ChatMessageDto;
import org.ict.intelligentclass.chat.model.dto.ChatroomDetailsDto;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.ict.intelligentclass.chat.model.dto.ChatMessagesResponse;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

    public ChatroomEntity insertRoom(Map<Integer, String> people, String roomType) {
        log.info("insertRoom people = " + people);

        String roomName = String.join(", ", people.values());
        String creator = people.get(0);

        ChatroomEntity chatroomEntity = new ChatroomEntity();
        chatroomEntity.setRoomName(roomName);
        chatroomEntity.setRoomType(roomType.equals("groups") ? "group" : "individual");
        chatroomEntity.setCreatedAt(new Date());
        chatroomEntity.setCreator(creator);

        ChatroomEntity newRoom = chatroomRepository.save(chatroomEntity);
        log.info(newRoom.toString());

        Long roomId = newRoom.getRoomId();

        for (String userId : people.values()) {
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

        List<Long> roomIds = chatUserRepository.findRoomIdsByUserIdOrderByIsPinned(userId);
        List<ChatroomEntity> chatrooms = chatroomRepository.findByRoomIdIn(roomIds);

        List<ChatroomDetailsDto> chatroomDetails = new ArrayList<>();

        for (ChatroomEntity chatroom : chatrooms) {
            ChatUserEntity chatUser = chatUserRepository.findByChatUserCompositeKeyUserIdAndChatUserCompositeKeyRoomId(userId, chatroom.getRoomId());
            ChatMessageEntity latestMessage = chatMessageRepository.findTopByRoomIdOrderByDateSentDesc(chatroom.getRoomId());
            Date latestMessageTimestamp = latestMessage != null ? latestMessage.getDateSent() : null;
            Long totalMessages = chatMessageRepository.countByRoomId(chatroom.getRoomId());
            Long readMessages = messageReadRepository.countByRoomIdAndUserId(chatroom.getRoomId(), userId);
            Long unreadMessageCount = totalMessages - readMessages;

            chatroomDetails.add(new ChatroomDetailsDto(chatroom, chatUser, latestMessage, latestMessageTimestamp, unreadMessageCount));
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
        // Check if there is an announcement message
        Optional<ChatMessageEntity> announcementOpt = chatMessageRepository.findFirstByRoomIdAndIsAnnouncement(roomId, 1L);
        ChatMessageDto announcementDto = null;
        if (announcementOpt.isPresent()) {
            announcementDto = convertToDto(announcementOpt.get(), userId, roomId);
        }

        // Fetch messages
        List<ChatMessageEntity> messages = chatMessageRepository.findByRoomId(roomId, PageRequest.of(page - 1, 25, Sort.by("dateSent").ascending()));

        // Process messages and mark them as read
        List<ChatMessageDto> messageDtos = messages.stream()
                .map(message -> {
                    markMessageAsRead(userId, roomId, message);
                    return convertToDto(message, userId, roomId);
                })
                .collect(Collectors.toList());

        // Handle no messages scenario
        if (messageDtos.isEmpty() && announcementDto == null) {
            return new ChatMessagesResponse(List.of(), null);
        }

        return new ChatMessagesResponse(messageDtos, announcementDto);
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


    public ChatMessageEntity saveMessage(ChatMessageEntity chatMessageEntity) {

        chatMessageEntity.setDateSent(new Date());

        switch (chatMessageEntity.getMessageType().intValue()) {
            case 0: // Text message
                // Additional logic for text messages if needed
                break;
            case 1: // Photo
                // Additional logic for photos if needed
                break;
            case 2: // Video
                // Additional logic for videos if needed
                break;
            case 3: // File
                // Additional logic for files if needed
                break;
            default:
                throw new IllegalArgumentException("Unsupported message type");
        }

        ChatMessageEntity savedMessage = chatMessageRepository.save(chatMessageEntity);

        // Mark message as read by sender
        MessageReadEntity messageReadEntity = new MessageReadEntity();
        messageReadEntity.setMessageId(savedMessage.getMessageId());
        messageReadEntity.setUserId(chatMessageEntity.getSenderId());
        messageReadEntity.setRoomId(chatMessageEntity.getRoomId());
        messageReadEntity.setReadAt(new Date());

        messageReadRepository.save(messageReadEntity);

        return savedMessage;

    }
}


//    public ArrayList<ChatUserEntity> selectChatrooms(String userId) {
//        log.info("selectChatrooms userId = " + userId);
//
//    }

