package org.ict.intelligentclass.chat.jpa.repository;

import org.ict.intelligentclass.chat.jpa.entity.MessageReadCompositeKey;
import org.ict.intelligentclass.chat.jpa.entity.MessageReadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageReadRepository extends JpaRepository<MessageReadEntity, MessageReadCompositeKey> {

    @Query("SELECT COUNT(m) FROM MessageReadEntity m WHERE m.roomId = :roomId")
    long countByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT COUNT(m) FROM MessageReadEntity m WHERE m.roomId = :roomId AND m.messageReadCompositeKey.userId = :userId")
    long countByRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") String userId);

    @Query("SELECT COUNT(m) FROM MessageReadEntity m WHERE m.messageReadCompositeKey.messageId = :messageId")
    long countByMessageReadCompositeKeyMessageId(@Param("messageId") Long messageId);

    boolean existsByMessageReadCompositeKey(MessageReadCompositeKey compositeKey);

    void deleteByRoomId(Long roomId);
}
