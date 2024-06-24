package org.ict.intelligentclass.chat.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.ict.intelligentclass.chat.jpa.entity.ChatMessageEntity;
import org.ict.intelligentclass.chat.model.dto.ChatMessageDto;
import org.ict.intelligentclass.chat.model.service.ChatService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class WebSocketMessageHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = new HashSet<>();
    private ChatService chatService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("웹소켓 연결 설정됨: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("웹소켓이 받은 메시지: " + message.getPayload());

        ChatMessageDto chatMessageDto = objectMapper.readValue(message.getPayload(), ChatMessageDto.class);

        // Save the message to the database
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setRoomId(chatMessageDto.getRoomId());
        chatMessageEntity.setSenderId(chatMessageDto.getSenderId());
        chatMessageEntity.setMessageContent(chatMessageDto.getMessageContent());
        chatMessageEntity.setMessageType(chatMessageDto.getMessageType());
        chatMessageEntity.setDateSent(new Date()); // You can also use chatMessageDto.getDateSent() if set from the client
        chatMessageEntity.setIsAnnouncement(chatMessageDto.isAnnouncement() ? 1L : 0L);

        ChatMessageEntity savedMessage = chatService.saveMessage(chatMessageEntity);

        // Convert saved message to DTO
        ChatMessageDto messageDto = convertToDto(savedMessage);

        // Broadcast the message
        for (WebSocketSession webSocketSession : sessions) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageDto)));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("웹소켓 연결 종료: " + session.getId());
        sessions.remove(session);
    }

    private ChatMessageDto convertToDto(ChatMessageEntity message) {
        return ChatMessageDto.builder()
                .messageId(message.getMessageId())
                .roomId(message.getRoomId())
                .senderId(message.getSenderId())
                .messageContent(message.getMessageContent())
                .messageType(message.getMessageType())
                .dateSent(message.getDateSent())
                .isAnnouncement(message.getIsAnnouncement() == 1)
                .readCount(0) // Initialize with 0, update as needed
                .isReadByCurrentUser(false) // Initialize with false, update as needed
                .files(new ArrayList<>()) // Initialize with empty list, update as needed
                .build();
    }
}
