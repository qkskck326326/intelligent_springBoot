package org.ict.intelligentclass.security.controller;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.security.entity.LoginTokenEntity;
import org.ict.intelligentclass.security.jwt.util.JwtTokenUtil;
import org.ict.intelligentclass.security.service.LoginTokenService;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController // 이 클래스가 REST 컨트롤러임을 나타내며, Spring MVC에서 HTTP 요청을 처리하는 핸들러가 됩니다.
@Slf4j // Lombok의 로깅을 위한 어노테이션, 이 클래스 내부에서는 로그를 찍는 부분이 생략되어 있지만, 로깅 목적으로 사용됩니다.
public class AccessTokenReissueController {

    private final JwtTokenUtil jwtTokenUtil; // JWT 처리를 위한 유틸리티 클래스
    private final UserService userService; // 사용자 정보를 처리하는 서비스
    private final LoginTokenService loginTokenService; // 리프레시 토큰 관련 서비스

    @PostMapping("/reissue") // POST 요청을 '/reissue' 경로로 매핑합니다.
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        // HTTP 요청에서 'Authorization' 헤더를 통해 리프레시 토큰을 받아옵니다.
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) { // 토큰이 없거나 Bearer 타입이 아니면 에러 반환
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        String refreshTokenValue = authorizationHeader.substring("Bearer ".length()); // 실제 토큰 값을 추출합니다.

        // 토큰 만료 여부 검사
        try {
            if (jwtTokenUtil.isTokenExpired(refreshTokenValue)) {
                // 리프레시 토큰이 만료되면 데이터베이스에서 삭제합니다.
                loginTokenService.deleteByRefreshTokenValue(refreshTokenValue);
                return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
            }
        } catch (ExpiredJwtException e) { // 정상적이지 않은 경우(토큰 만료 검사의 결과값이 true or false 가 아닌경우!!!)
            // 리프레시 토큰이 만료되면 데이터베이스에서 삭제합니다.
            loginTokenService.deleteByRefreshTokenValue(refreshTokenValue);
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 리프레시 토큰이 맞는지 카테고리로 확인합니다.
        String category = jwtTokenUtil.getTokenTypeFromToken(refreshTokenValue);
        if (!category.equals("refresh")) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // 토큰에서 사용자 이메일을 추출합니다.
        String userEamil = jwtTokenUtil.getUserEmailFromToken(refreshTokenValue);
        String provider = jwtTokenUtil.getProviderFromToken(refreshTokenValue);

        //
        UserId userId = new UserId(userEamil, provider);
        Optional<UserEntity> optionalUser = userService.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }

        // 리프레시 토큰이 유효한지 확인합니다.
        Optional<LoginTokenEntity> refreshTokenOptional = loginTokenService.findByTokenValue(refreshTokenValue);
        if (refreshTokenOptional.isEmpty() || !refreshTokenOptional.get().getUser().equals(optionalUser.get())) {
            return new ResponseEntity<>("refresh token not found or does not match", HttpStatus.BAD_REQUEST);
        }

//        // 리프레시 토큰의 상태 검증
//        LoginTokenEntity loginTokenEntity = refreshTokenOptional.get();
//        if (!loginTokenEntity.getStatus().equals("activated")) {
//            return new ResponseEntity<>("refresh token invalid or expired", HttpStatus.BAD_REQUEST);
//        }

        // 새로운 액세스 토큰 생성
        // 액세스 토큰의 유효 시간 (밀리초 단위)
        Long accessExpiredMs = 600000L;
        String access = jwtTokenUtil.generateToken(userEamil, provider, "access", accessExpiredMs);

        // 응답에 새로운 액세스 토큰 추가
        response.addHeader("Authorization", "Bearer " + access);

        // 클라이언트가 Authorization 헤더를 읽을 수 있도록 설정
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        // 성공적으로 새 토큰을 발급받았을 때의 응답
        return new ResponseEntity<>(HttpStatus.OK);
    }
}