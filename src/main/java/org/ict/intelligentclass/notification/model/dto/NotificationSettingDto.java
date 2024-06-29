package org.ict.intelligentclass.notification.model.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.notification.jpa.entity.NotificationSettingEntity;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class NotificationSettingDto {

    private int settingId;
    private String nickname;
    private int notificationType;
    private String isEnabled;

    public NotificationSettingEntity toEntity() {
        return NotificationSettingEntity.builder()
               .settingId(this.settingId)
               .nickname(this.nickname)
               .notificationType(this.notificationType)
               .isEnabled(this.isEnabled)
               .build();
    }

}
