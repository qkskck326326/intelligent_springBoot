package org.ict.intelligentclass.chat.model.service;


import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.ChatMessageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToSpecificRoom(Long roomId, ChatMessageEntity message) {
        log.info("Sending message to room {}: {}", roomId, message);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, message);
    }
}
