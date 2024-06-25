package org.ict.intelligentclass.chat.model.service;


import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.ChatMessageEntity;
import org.ict.intelligentclass.chat.model.dto.ChatMessageDto;
import org.ict.intelligentclass.chat.model.dto.RoomNameChangeDto;
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

    public void sendToSpecificRoom(Long roomId, ChatMessageDto messageDto) {
        log.info("Sending message to room {}: {}", roomId, messageDto);

        messagingTemplate.convertAndSend("/topic/room/" + roomId, messageDto);
    }

    public void sendRoomNameChange(Long roomId, RoomNameChangeDto roomNameChangeDto) {
        log.info("Sending room name change to room {}: {}", roomId, roomNameChangeDto);
        messagingTemplate.convertAndSend("/topic/room/" + roomId, roomNameChangeDto);
    }
}
