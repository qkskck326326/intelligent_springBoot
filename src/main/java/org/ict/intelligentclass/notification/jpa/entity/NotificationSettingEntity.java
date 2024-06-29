package org.ict.intelligentclass.notification.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.notification.model.dto.NotificationDto;
import org.ict.intelligentclass.notification.model.dto.NotificationSettingDto;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_NOTIFICATION_SETTING")
public class NotificationSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_setting_seq")
    @SequenceGenerator(name = "notification_setting_seq", sequenceName = "SQ_NOTIFICATION_SETTING_ID", allocationSize = 1)
    @Column(name = "SETTING_ID")
    private int settingId;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "NOTIFICATION_TYPE", nullable = false)
    private int notificationType;

    @Column(name = "IS_ENABLED", nullable = false)
    private String isEnabled;

    public NotificationSettingDto toDto() {
        return NotificationSettingDto.builder()
                .settingId(settingId)
                .nickname(nickname)
                .notificationType(notificationType)
                .isEnabled(isEnabled)
                .build();
    }

}
