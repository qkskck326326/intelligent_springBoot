package org.ict.intelligentclass.chat.jpa.repository;

import org.ict.intelligentclass.chat.jpa.entity.MessageFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageFileRepository extends JpaRepository<MessageFileEntity, Long> {

    List<MessageFileEntity> findByMessageId(Long messageId);
    void deleteByMessageId(Long messageId);

    void deleteByRoomId(Long roomId);

    List<MessageFileEntity> findByRoomId(Long roomId);
}
