package org.ict.intelligentclass.chat.jpa.repository;

import org.ict.intelligentclass.chat.jpa.entity.ChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    @Query("SELECT COUNT(c) FROM ChatMessageEntity c WHERE c.roomId = :roomId")
    long countByRoomId(@Param("roomId") Long roomId);

    ChatMessageEntity findTopByRoomIdOrderByDateSentDesc(Long roomId);

    List<ChatMessageEntity> findByRoomId(Long roomId, PageRequest dateSent);

    Optional<ChatMessageEntity> findFirstByRoomIdAndIsAnnouncement(Long roomId, long l);

    void deleteByRoomId(Long roomId);

    @Query("SELECT m FROM ChatMessageEntity m WHERE m.roomId = :roomId AND m.isAnnouncement = 1")
    ChatMessageEntity findAnnouncementByRoomId(Long roomId);
}
