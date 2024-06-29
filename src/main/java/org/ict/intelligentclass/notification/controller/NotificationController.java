package org.ict.intelligentclass.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.notification.model.dto.NotificationDto;
import org.ict.intelligentclass.notification.model.dto.NotificationSettingDto;
import org.ict.intelligentclass.notification.model.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    private final NotificationService notificationService;

    // 유저의 알림 목록
    @GetMapping("/{nickname}")
    public ResponseEntity<List<NotificationDto>> getNotifications(@PathVariable String nickname) {
        List<NotificationDto> notifications = notificationService.getNotificationsByNickname(nickname);
        return ResponseEntity.ok(notifications);
    }

    // 알림 삭제
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@PathVariable int notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    // 알림 설정 목록
    @GetMapping("/settings/{nickname}")
    public ResponseEntity<List<NotificationSettingDto>> getNotificationSettings(@PathVariable String nickname) {
        List<NotificationSettingDto> settings = notificationService.getNotificationSettings(nickname);
        return ResponseEntity.ok(settings);
    }

    // 알림 설정
    @PutMapping("/settings/{nickname}")
    public ResponseEntity<Void> updateNotificationSetting(@PathVariable String nickname, @RequestBody NotificationSettingDto settingDto) {
        notificationService.updateNotificationSetting(nickname, settingDto);
        return ResponseEntity.noContent().build();
    }
}
