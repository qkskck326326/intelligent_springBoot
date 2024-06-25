package org.ict.intelligentclass.chat.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomNameChangeDto {

    private Long roomId;
    private String roomName;
}
