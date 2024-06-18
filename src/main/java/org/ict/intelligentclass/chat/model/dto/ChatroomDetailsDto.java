package org.ict.intelligentclass.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.ChatMessageEntity;
import org.ict.intelligentclass.chat.jpa.entity.ChatUserEntity;
import org.ict.intelligentclass.chat.jpa.entity.ChatroomEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@Slf4j
@Builder
public class ChatroomDetailsDto {

    private ChatroomEntity chatroom;
    private ChatUserEntity chatUser;
    private ChatMessageEntity latestMessage;
    private Date latestMessageTimestamp;
    private Long unreadMessageCount;

}
