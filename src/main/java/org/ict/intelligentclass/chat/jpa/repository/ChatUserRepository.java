package org.ict.intelligentclass.chat.jpa.repository;

import org.ict.intelligentclass.chat.jpa.entity.ChatUserCompositeKey;
import org.ict.intelligentclass.chat.jpa.entity.ChatUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUserEntity, ChatUserCompositeKey> {

    @Query("SELECT c.chatUserCompositeKey.roomId FROM ChatUserEntity c WHERE c.chatUserCompositeKey.userId = :userId")
    List<Long> findRoomIdsByUserId(@Param("userId") String userId);

    @Query("SELECT cue.chatUserCompositeKey.roomId FROM ChatUserEntity cue WHERE cue.chatUserCompositeKey.userId = :userId ORDER BY cue.isPinned ASC")
    List<Long> findRoomIdsByUserIdOrderByIsPinned(@Param("userId") String userId);

    ChatUserEntity findByChatUserCompositeKeyUserIdAndChatUserCompositeKeyRoomId(String userId, Long roomId);

    @Query("SELECT COUNT(c) FROM ChatUserEntity c WHERE c.chatUserCompositeKey.roomId = :roomId")
    int countByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT c.chatUserCompositeKey.userId FROM ChatUserEntity c WHERE c.chatUserCompositeKey.roomId = :roomId")
    List<String> findUserIdsByRoomId(@Param("roomId") Long roomId);
}
