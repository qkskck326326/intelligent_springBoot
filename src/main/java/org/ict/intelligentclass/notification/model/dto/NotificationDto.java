package org.ict.intelligentclass.notification.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.notification.jpa.entity.NotificationEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class NotificationDto {

    private int notificationId;
    private String nickname;
    private String otherNickname;
    private int type;
    private String notificationContent;
    private Date notificationDate;
    private String notificationLink;

    public NotificationEntity toEntity() {
        return NotificationEntity.builder()
               .notificationId(this.notificationId)
               .nickname(this.nickname)
               .otherNickname(this.otherNickname)
               .type(this.type)
               .notificationContent(this.notificationContent)
               .notificationDate(this.notificationDate)
               .notificationLink(this.notificationLink)
               .build();
    }
}
