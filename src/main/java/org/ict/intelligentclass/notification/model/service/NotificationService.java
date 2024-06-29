package org.ict.intelligentclass.notification.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.notification.jpa.entity.NotificationEntity;
import org.ict.intelligentclass.notification.jpa.entity.NotificationSettingEntity;
import org.ict.intelligentclass.notification.jpa.repository.NotificationRepository;
import org.ict.intelligentclass.notification.jpa.repository.NotificationSettingRepository;
import org.ict.intelligentclass.notification.model.dto.NotificationDto;
import org.ict.intelligentclass.notification.model.dto.NotificationSettingDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    
    // 알림 목록
    public List<NotificationDto> getNotificationsByNickname(String nickname) {
        List<NotificationEntity> notifications = notificationRepository.findByNickname(nickname);
        return notifications.stream()
                .map(notification -> NotificationDto.builder()
                        .notificationId(notification.getNotificationId())
                        .nickname(notification.getNickname())
                        .otherNickname(notification.getOtherNickname())
                        .type(notification.getType())
                        .notificationContent(notification.getNotificationContent())
                        .notificationDate(notification.getNotificationDate())
                        .notificationLink(notification.getNotificationLink())
                        .build())
                .collect(Collectors.toList());
    }
    
    // 알림 삭제
    public void deleteNotification(int notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    // 알림 설정 목록
    public List<NotificationSettingDto> getNotificationSettings(String nickname) {
        List<NotificationSettingEntity> settings = notificationSettingRepository.findByNickname(nickname);
        return settings.stream()
                .map(setting -> new NotificationSettingDto(
                        setting.getSettingId(),
                        setting.getNickname(),
                        setting.getNotificationType(),
                        setting.getIsEnabled()))
                .collect(Collectors.toList());
    }

    // 알림 설정
    public void updateNotificationSetting(String nickname, NotificationSettingDto settingDto) {
        NotificationSettingEntity setting = notificationSettingRepository.findByNicknameAndNotificationType(nickname, settingDto.getNotificationType());
        setting.setIsEnabled(settingDto.getIsEnabled());
        notificationSettingRepository.save(setting);
    }

}
