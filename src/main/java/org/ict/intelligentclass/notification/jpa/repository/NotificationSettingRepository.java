package org.ict.intelligentclass.notification.jpa.repository;

import org.ict.intelligentclass.notification.jpa.entity.NotificationSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationSettingRepository extends JpaRepository<NotificationSettingEntity, Integer> {

    List<NotificationSettingEntity> findByNickname(String nickname);
    NotificationSettingEntity findByNicknameAndNotificationType(String nickname, int notificationType);
}
