package org.ict.intelligentclass.chat.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_CHATROOM")
public class ChatroomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_ROOM_ID")
    @SequenceGenerator(name = "SQ_ROOM_ID", sequenceName = "SQ_ROOM_ID", allocationSize = 1)
    @Column(name="ROOM_ID") //채팅방 일련번호
    private Long roomId;

    @Column(name="ROOM_NAME") //채팅방 이름 (없을 시 상대방 이름)
    private String roomName;

    @Column(name="ROOM_TYPE") //채팅방 종류 (개인/단체)
    private String roomType;

    @Column(name="CREATED_AT") //방 생성일
    private Date createdAt;

    @Column(name="CREATOR") //방 생성자 닉네임
    private String creator;

}
