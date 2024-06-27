package org.ict.intelligentclass.chat.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_MESSAGEREAD")
@Data
public class MessageReadEntity {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "messageId", column = @Column(name = "MESSAGE_ID")),
            @AttributeOverride(name = "userId", column = @Column(name = "USER_ID"))
    })
    private MessageReadCompositeKey messageReadCompositeKey; //유저닉네임, 채팅방 일련번호 묶음

    @Column(name="ROOM_ID", nullable = false)//채팅방
    private Long roomId;

    @Column(name="READ_AT", nullable = false)//읽은 시간
    private Date readAt;
}
