package org.ict.intelligentclass.security.handler;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.security.entity.LoginTokenEntity;
import org.ict.intelligentclass.security.jwt.util.JwtTokenUtil;
import org.ict.intelligentclass.security.service.LoginTokenService;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {
    private final JwtTokenUtil jwtTokenUtil;
    private final LoginTokenService loginTokenService;
    private final UserService userService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) { // null이 아니거나 Bearer로 시작하는지
            String tokenValue = authorizationHeader.substring(7); // 'Bearer ' 문자 제거

            try {
                jwtTokenUtil.isTokenExpired(tokenValue);
            } catch (ExpiredJwtException e) {
                log.info("Token expired during logout: {}", e.getMessage());
                // HTTP 응답을 설정하여 직접 클라이언트에게 오류 정보를 전달합니다.
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                response.setContentType("application/json");
                try {
                    response.getWriter().write("{\"error\":\"Session has expired. Please log in again.\"}");
                    response.getWriter().flush();
                } catch (IOException ioException) {
                    log.error("Error writing to response", ioException);
                }
                return; // 메서드를 빠져나와 추가 처리를 중단합니다.
            }

            // 만료 여부와 상관없이 사용자 정보를 조회하여 로그아웃 처리를 합니다.
            UserId userId = jwtTokenUtil.getUserIdFromToken(tokenValue);
            Optional<UserEntity> optionalUser = userService.findByUserId(userId);
            //Optional<UserEntity> optionalUser = userRepository.findByEmailAndProvider(userId.getUserEmail(),userId.getProvider());
            log.info("if문 전 확인 : " , optionalUser);
            if (optionalUser.isPresent()) {
                UserEntity userEntity = optionalUser.get();
                log.info("if문 안 확인 : " , userEntity);
                // 카카오 로그아웃 처리
                if ("kakao".equals(userEntity.getUserId().getProvider())) {
                    String kakaoAccessToken = userEntity.getSnsAccessToken(); // 저장된 카카오 토큰 사용
                    String kakaoLogoutUrl = "https://kapi.kakao.com/v1/user/logout";
                    HttpHeaders headers = new HttpHeaders();
                    headers.set("Authorization", "Bearer " + kakaoAccessToken);

                    HttpEntity<String> kakaoRequestEntity = new HttpEntity<>(headers);
                    RestTemplate restTemplate = new RestTemplate();
                    ResponseEntity<String> kakaoResponse = restTemplate.exchange(kakaoLogoutUrl, HttpMethod.POST, kakaoRequestEntity, String.class);
                    log.info("Kakao logout response = {}", kakaoResponse.getBody());
                }

                Optional<LoginTokenEntity> optionalToken = loginTokenService.findByEmailAndIdProvider(userEntity.getUserId());
                optionalToken.ifPresent(refreshToken -> loginTokenService.deleteByRefreshTokenValue(refreshToken.getRefreshTokenValue()));
            }
        }

        // 성공적인 로그아웃 응답을 설정합니다.
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
