package org.ict.intelligentclass.chat.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageFileDto {

    private Long fileId;
    private Long messageId;
    private String senderId;
    private Long roomId;
    private String fileURL;
    private String fileSize;
    private String originalName;
    private String renamedName;

}