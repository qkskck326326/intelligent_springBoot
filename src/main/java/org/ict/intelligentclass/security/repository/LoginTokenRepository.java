package org.ict.intelligentclass.security.repository;


import org.ict.intelligentclass.security.entity.LoginTokenEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LoginTokenRepository extends JpaRepository<LoginTokenEntity, UserId> {
    //void deleteByUserEmailAndIdProvider(String userEmail, String provider);

    Optional<LoginTokenEntity> findByRefreshTokenValue(String refreshTokenValue);
    Boolean existsByRefreshTokenValue(String refreshTokenValue);
    void deleteByRefreshTokenValue(String refreshTokenValue);
    // Optional<RefreshToken> findByUserId(UUID id);

    @Query("SELECT l FROM LoginTokenEntity l WHERE l.userId.userEmail = :email AND l.userId.provider = :provider")
    Optional<LoginTokenEntity> findByEmailAndIdProvider(@Param("email") String email, @Param("provider") String provider);
}

