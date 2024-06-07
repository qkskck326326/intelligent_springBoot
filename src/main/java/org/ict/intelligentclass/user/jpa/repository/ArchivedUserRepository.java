package org.ict.intelligentclass.user.jpa.repository;


import org.ict.intelligentclass.user.jpa.entity.ArchivedUserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArchivedUserRepository extends JpaRepository<ArchivedUserEntity, UserId> {
    // save(ArchiveUserEntity)

    @Query("SELECT au FROM ArchivedUserEntity au WHERE au.userId.userEmail = :email")
    Optional<ArchivedUserEntity> findByEmail(@Param("email") String email);


    @Query("SELECT au FROM ArchivedUserEntity au WHERE au.userId.userEmail = :email AND au.userId.provider = :provider")
    Optional<ArchivedUserEntity> findByEmailAndProvider(@Param("email") String email, @Param("provider") String provider);
}