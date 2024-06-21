package org.ict.intelligentclass.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.ChatMessageEntity;
import org.ict.intelligentclass.chat.jpa.entity.ChatUserEntity;
import org.ict.intelligentclass.chat.jpa.entity.ChatroomEntity;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
public class ChatroomDetailsDto {

    private ChatroomEntity chatroom;
    private ChatUserEntity chatUser;
    private ChatMessageEntity latestMessage;
    private int totalPeople;
    private Date latestMessageTimestamp;
    private Long unreadMessageCount;
    private List<UserEntity> users;

}
