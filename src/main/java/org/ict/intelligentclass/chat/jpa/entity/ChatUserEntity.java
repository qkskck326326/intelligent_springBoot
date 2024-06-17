package org.ict.intelligentclass.chat.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_CHATUSER")
@Builder
public class ChatUserEntity {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "userId", column = @Column(name = "USER_ID")),
            @AttributeOverride(name = "roomId", column = @Column(name = "ROOM_ID"))
    })
    private ChatUserCompositeKey chatUserCompositeKey; //유저닉네임, 채팅방 일련번호 묶음

    @Column(name="IS_MUTED", nullable = false, columnDefinition = "0")
    private Long isMuted; //알림 여부(0: 알림킴(기본) 1: 끔)

    @Column(name="IS_PINNED", nullable = false, columnDefinition = "0")
    private Long isPinned; //핀 여부 (0: 핀없음(기본) 1: 핀있음)

}
