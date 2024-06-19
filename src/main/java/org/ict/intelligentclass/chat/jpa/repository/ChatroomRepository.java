package org.ict.intelligentclass.chat.jpa.repository;

import org.ict.intelligentclass.chat.jpa.entity.ChatroomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatroomRepository extends JpaRepository<ChatroomEntity, Long> {

    List<ChatroomEntity> findByRoomIdIn(List<Long> roomIds);

    @Query("SELECT COUNT(cu) FROM ChatUserEntity cu WHERE cu.chatUserCompositeKey.roomId = :roomId")
    int countUsersInRoom(@Param("roomId") Long roomId);
}
