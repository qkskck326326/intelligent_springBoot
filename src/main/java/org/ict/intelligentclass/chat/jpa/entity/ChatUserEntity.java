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
    private ChatUserCompositeKey chatUserCompositeKey;
}
