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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_CHATMESSAGE")
public class ChatMessageEntity {

    @Id
    @Column(name="MESSAGE_ID") //메시지 일련번호
    private Long messageId;

    @Column(name="ROOM_ID", nullable = false) //채팅방 일련번호
    private Long roomId;

    @Column(name="SENDER_ID", nullable = false) //송신자 닉네임
    private String senderId;

    @Column(name="MESSAGE_CONTENT") //메시지 내용
    private String messageContent;

    @Column(name="MESSAGE_TYPE", nullable = false) //메시지 타입(0: 메시지 1: 사진 2: 동영상 3:그 외)
    private Long messageType;

    @Column(name="DATE_SENT", nullable = false) //전송 시간
    private Date dateSent;

    @Column(name="IS_ANNOUNCEMENT", nullable = false) //공지사항 여부(0:아님(기본) 1:맞음)
    private Long isAnnouncement;
}
