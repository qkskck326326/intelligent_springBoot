package org.ict.intelligentclass.security.service;

import lombok.RequiredArgsConstructor;

import org.ict.intelligentclass.security.entity.LoginTokenEntity;
import org.ict.intelligentclass.security.repository.LoginTokenRepository;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginTokenService {
    private final LoginTokenRepository loginTokenRepository;

//    public void createOrUpdateToken(String userEmail, String provider, String accessToken, String refreshToken) {
//        UserId userId = new UserId(userEmail, provider);
//        LoginTokenEntity loginTokenEntity = LoginTokenEntity.builder()
//                .id(userId)
////                .accessTokenValue(accessToken)
//                .refreshTokenValue(refreshToken)
//                .build();
//        loginTokenRepository.save(loginTokenEntity);
//    }

//    public void updateAccessToken(String userEmail, String provider, String accessToken) {
//        LoginTokenEntity loginTokenEntity = loginTokenRepository.findByEmailAndIdProvider(userEmail, provider);
//
//        loginTokenEntity.setAccessTokenValue(accessToken);
//        loginTokenRepository.save(loginTokenEntity);
//    }

//    @Transactional
//    public void deleteTokenByEmailAndIdProvider(String userEmail, String provider) {
//        loginTokenRepository.deleteByUserEmailAndIdProvider(userEmail, provider);
//    }




    public void save(LoginTokenEntity loginTokenEntity) {
        loginTokenRepository.save(loginTokenEntity); // insert 실행 처리
    }

    public Optional<LoginTokenEntity> findByTokenValue(String tokenValue) {
        return loginTokenRepository.findByRefreshTokenValue(tokenValue); // select 조회 : 토큰객체 조회(토큰값)
    }

    public Boolean existsByRefresh(String tokenValue) {
        return loginTokenRepository.existsByRefreshTokenValue(tokenValue); // select 조회 : 토큰객체 존재여부 확인
    }

    public void deleteByRefreshTokenValue(String tokenValue) {
        loginTokenRepository.deleteByRefreshTokenValue(tokenValue);
    }

    public Optional<LoginTokenEntity> findByEmailAndIdProvider(UserId userId) {
        return loginTokenRepository.findByEmailAndIdProvider(userId.getUserEmail(), userId.getProvider());
    }


}