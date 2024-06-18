package org.ict.intelligentclass.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatMessagesResponse {

    private List<ChatMessageDto> messages;
    private ChatMessageDto announcement;

}
