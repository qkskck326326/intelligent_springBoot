package org.ict.intelligentclass.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessagesResponse {

    private ChatMessageDto announcement;
    private List<ChatMessageDto> messages;

}
