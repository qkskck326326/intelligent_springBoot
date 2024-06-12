package org.ict.intelligentclass.chat.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Id
    @Column(name="MESSAGE_ID")//메시지 일련번호
    private Long messageId;

    @Column(name="USER_ID", nullable = false)//읽은 유저
    private String userId;

    @Column(name="ROOM_ID", nullable = false)//채팅방
    private Long roomId;

    @Column(name="READ_AT", nullable = false)//읽은 시간
    private Date readAt;
}
