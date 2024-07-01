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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/reissue")
public class AccessTokenReissueController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final LoginTokenService loginTokenService;

    @PostMapping
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader("Authorization");
        log.info("Received reissue request with header: " + authorizationHeader);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.error("Invalid reissue request: no refresh token found in header");
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }


        String refreshTokenValue = authorizationHeader.substring("Bearer ".length());

        try {
            if (jwtTokenUtil.isTokenExpired(refreshTokenValue)) {
                loginTokenService.deleteByRefreshTokenValue(refreshTokenValue);
                log.error("Refresh token expired: " + refreshTokenValue);
                return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
            }
        } catch (ExpiredJwtException e) {
            loginTokenService.deleteByRefreshTokenValue(refreshTokenValue);
            log.error("Refresh token expired with exception: " + refreshTokenValue);
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        String category = jwtTokenUtil.getTokenTypeFromToken(refreshTokenValue);
        if (!category.equals("refresh")) {
            log.error("Invalid refresh token type: " + refreshTokenValue);
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String userEmail = jwtTokenUtil.getUserEmailFromToken(refreshTokenValue);
        String provider = jwtTokenUtil.getProviderFromToken(refreshTokenValue);

        UserId userId = new UserId(userEmail, provider);
        Optional<UserEntity> optionalUser = userService.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            log.error("User not found for token: " + refreshTokenValue);
            return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
        }

        Optional<LoginTokenEntity> refreshTokenOptional = loginTokenService.findByTokenValue(refreshTokenValue);
        if (refreshTokenOptional.isEmpty() || !refreshTokenOptional.get().getUser().equals(optionalUser.get())) {
            log.error("Refresh token not found or does not match user: " + refreshTokenValue);
            return new ResponseEntity<>("refresh token not found or does not match", HttpStatus.BAD_REQUEST);
        }

        Long accessExpiredMs = 600000L; // 10분
        String newAccessToken = jwtTokenUtil.generateToken(userEmail, provider, "access", accessExpiredMs);

        // 리프레시 토큰의 만료 시간이 30초 이하로 남았는지 확인
        Date refreshTokenExpiryDate = jwtTokenUtil.getExpirationDateFromToken(refreshTokenValue);
        long currentTimeMillis = System.currentTimeMillis();
        long refreshTokenExpiryTimeMillis = refreshTokenExpiryDate.getTime();
        long remainingTimeMillis = refreshTokenExpiryTimeMillis - currentTimeMillis;

        // 30초 이하로 남았다면 새로운 리프레시 토큰 발급
        String newRefreshToken = refreshTokenValue;
        if (remainingTimeMillis <= 30000L) {
            Long refreshExpiredMs = 86400000L; // 1일

            // Long refreshExpiredMs = 604800000L; // 7일
            newRefreshToken = jwtTokenUtil.generateToken(userEmail, provider, "refresh", refreshExpiredMs);

            // 기존 리프레시 토큰 삭제 및 새 리프레시 토큰 저장
            loginTokenService.deleteByRefreshTokenValue(refreshTokenValue);
            LoginTokenEntity newLoginTokenEntity = LoginTokenEntity.builder()
                    .userId(userId)
                    .refreshTokenValue(newRefreshToken)
                    .build();
            loginTokenService.save(newLoginTokenEntity);
            log.info("리프레쉬 토큰 재발급 성공!!!!!!!!!");
        }

        response.addHeader("Authorization", "Bearer " + newAccessToken);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("refresh", newRefreshToken);  // 재발급한 refresh토큰

        log.info("Reissue request successful");

        try {
            // 응답 데이터를 JSON으로 변환하여 응답 바디에 추가
            String responseBodyJson = new ObjectMapper().writeValueAsString(responseBody);
            response.setContentType("application/json");
            response.getWriter().write(responseBodyJson);
            response.getWriter().flush();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (JsonProcessingException e) {
            log.error("JSON processing error", e);
            return new ResponseEntity<>("JSON processing error", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            log.error("IO error", e);
            return new ResponseEntity<>("IO error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
