package org.ict.intelligentclass.notification.jpa.repository;

import org.ict.intelligentclass.notification.jpa.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {

    List<NotificationEntity> findByNickname(String nickname);
}
