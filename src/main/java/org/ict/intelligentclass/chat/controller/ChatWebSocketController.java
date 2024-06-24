package org.ict.intelligentclass.chat.controller;

import org.ict.intelligentclass.chat.model.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.ict.intelligentclass.chat.jpa.entity.ChatMessageEntity;
import org.ict.intelligentclass.chat.model.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final WebSocketService webSocketService;
    private ChatService chatService;

    @Autowired
    public ChatWebSocketController(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Autowired
    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }


    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessageEntity sendMessage(ChatMessageEntity message) {
        // Save message to the database
        ChatMessageEntity savedMessage = chatService.saveMessage(message);
        return savedMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessageEntity addUser(ChatMessageEntity message) {
        message.setMessageContent(message.getSenderId() + " joined the chat");
        return message;
    }

    public void sendToSpecificRoom(Long roomId, ChatMessageEntity message) {
        webSocketService.sendToSpecificRoom(roomId, message);
    }
}
