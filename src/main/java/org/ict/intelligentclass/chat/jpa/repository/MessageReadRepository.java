package org.ict.intelligentclass.chat.jpa.repository;

import org.ict.intelligentclass.chat.jpa.entity.MessageReadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReadRepository extends JpaRepository<MessageReadEntity, Long> {

    @Query("SELECT COUNT(m) FROM MessageReadEntity m WHERE m.roomId = :roomId")
    long countByRoomId(@Param("roomId") Long roomId);

    long countByRoomIdAndUserId(Long roomId, String userId);

    int countByMessageId(Long messageId);

    boolean existsByMessageIdAndUserId(Long messageId, String userId);
}
