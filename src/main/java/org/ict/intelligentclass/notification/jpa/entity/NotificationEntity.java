package org.ict.intelligentclass.notification.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.notification.model.dto.NotificationDto;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_NOTIFICATION")
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @SequenceGenerator(name = "notification_seq", sequenceName = "SQ_NOTIFICATION_ID", allocationSize = 1)
    @Column(name = "NOTIFICATION_ID")
    private int notificationId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "OTHER_NICKNAME")
    private String otherNickname;

    @Column(name = "TYPE", nullable = false)
    private int type;

    @Column(name = "NOTIFICATION_CONTENT", nullable = false)
    private String notificationContent;

    @Column(name = "NOTIFICATION_DATE")
    private Date notificationDate;

    @Column(name = "NOTIFICATION_LINK", nullable = false)
    private String notificationLink;

    public NotificationDto toDto() {
        return NotificationDto.builder()
                .notificationId(notificationId)
                .nickname(nickname)
                .otherNickname(otherNickname)
                .type(type)
                .notificationContent(notificationContent)
                .notificationDate(notificationDate)
                .notificationLink(notificationLink)
                .build();
    }

}
