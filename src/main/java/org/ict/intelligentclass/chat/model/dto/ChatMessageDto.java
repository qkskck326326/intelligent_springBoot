package org.ict.intelligentclass.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessageDto {
    private Long messageId;
    private Long roomId;
    private String senderId;
    private String senderEmail;
    private String senderProfileImageUrl;
    private String messageContent;
    private Long messageType;
    private Date dateSent;
    private boolean isAnnouncement;
    private int readCount;
    private boolean isReadByCurrentUser;
    private int userType;
    private List<MessageFileDto> files;
}
