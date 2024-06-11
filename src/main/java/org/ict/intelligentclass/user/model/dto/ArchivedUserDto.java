package org.ict.intelligentclass.user.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.user.jpa.entity.ArchivedUserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArchivedUserDto {
    private String userEmail;
    private String provider;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime archivedTime;
    private String archivedReason;

    //entity -> dto method
    public ArchivedUserEntity toEntity() {
        UserId userId = new UserId(this.userEmail, this.provider);
        return ArchivedUserEntity.builder()
                .userId(userId)
                .userName(this.userName)
                .archivedTime(this.archivedTime)
                .archivedReason(this.archivedReason)
                .build();
    }
}
