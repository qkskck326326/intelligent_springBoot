package org.ict.intelligentclass.user.jpa.repository;



import org.ict.intelligentclass.user.jpa.entity.UserInterestEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserInterestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserInterestRepository extends JpaRepository<UserInterestEntity, UserInterestId> {
    // saveAll(List<UserInterestEntity>)

    @Query("SELECT ui FROM UserInterestEntity ui WHERE ui.user.nickname = :nickname")
    List<UserInterestEntity> findByUserNickname(@Param("nickname") String nickname);

    @Query("SELECT ui FROM UserInterestEntity ui WHERE ui.user.userId.userEmail = :email and ui.user.userId.provider = :provider")
    List<UserInterestEntity> findByInterestIdUserEmailAndInterestIdProvider(String email, String provider);
}