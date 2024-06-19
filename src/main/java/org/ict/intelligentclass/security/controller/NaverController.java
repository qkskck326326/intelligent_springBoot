package org.ict.intelligentclass.security.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.security.entity.LoginTokenEntity;
import org.ict.intelligentclass.security.jwt.util.JwtTokenUtil;
import org.ict.intelligentclass.security.service.LoginTokenService;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.ict.intelligentclass.user.model.dto.UserDto;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/naver")
@RequiredArgsConstructor
@CrossOrigin
public class NaverController {
    private final UserService userService;
    private final LoginTokenService loginTokenService;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    @PostMapping
    public ResponseEntity<?> naverLogin(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String accessToken = request.get("token");

        if (accessToken == null) {
            return ResponseEntity.badRequest().body("Access token is missing");
        }

        // 네이버 API를 호출하여 사용자 정보를 가져옵니다.
        Map<String, Object> userInfo = getNaverUserInfo(accessToken);
        if (userInfo == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token");
        }

        String userEmail = (String) userInfo.get("email");
//        Optional<UserEntity> existingUser = userRepository.findByEmailAndProvider(userEmail, "naver");
        Optional<UserEntity> optionalUser = userService.findByUserEmailAndProvider(userEmail, "naver");


        if (optionalUser.isPresent()) { // 유저가 이미 존재하면 로그인 처리
            UserEntity userEntity = optionalUser.get();
            userEntity.setSnsAccessToken(accessToken);

            return handleLogin(userEntity, response);
        } else { // 유저가 존재하지 않으면 회원가입 처리

            return handleSignUp(userInfo, accessToken);
        }
    }

    private ResponseEntity<?> handleSignUp(Map<String, Object> userInfo, String accessToken) {
        String userName = decodeUnicodeString((String) userInfo.get("name"));

        UserDto userDto = new UserDto();
        userDto.setUserEmail((String) userInfo.get("email"));
        userDto.setProvider("naver");
        userDto.setUserName(userName);
        userDto.setUserPwd("");
        userDto.setPhone((String) userInfo.get("mobile"));
        userDto.setNickname("");
        userDto.setRegisterTime(LocalDateTime.now());
        userDto.setProfileImageUrl("https://intelliclassbucket.s3.ap-northeast-2.amazonaws.com/ProfileImages/defaultProfile.png");
        userDto.setUserType(0);
        userDto.setReportCount(0);
        userDto.setLoginOk('Y');
        userDto.setFaceLoginYn('N');
        userDto.setSnsAccessToken(accessToken);

        userService.insertSocialLoginUser(userDto.toEntity());
        log.info("회원가입 성공: {}, {}", userDto.getUserEmail(), userDto.getProvider());


        String logoutUrl = String.format(
                "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=%s&client_secret=%s&access_token=%s&service_provider=%s",
                clientId, clientSecret, accessToken, "NAVER"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseNaver;
        try {
            responseNaver = restTemplate.exchange(logoutUrl, HttpMethod.POST, requestEntity, String.class);
            log.info("Naver logout response: {}", responseNaver.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Naver logout failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Naver logout failed");
        }


        return ResponseEntity.ok("User registered successfully");
    }

    private ResponseEntity<?> handleLogin(UserEntity userEntity, HttpServletResponse response) {

        UserId userId = userEntity.getUserId();

        Long accessExpiredMs = 600000L;
        String accessToken = jwtTokenUtil.generateToken(userId.getUserEmail(), "naver", "access", accessExpiredMs);
        Long refreshExpiredMs = 86400000L;
        String refreshToken = jwtTokenUtil.generateToken(userId.getUserEmail(), "naver", "refresh", refreshExpiredMs);

        LoginTokenEntity loginTokenEntity = LoginTokenEntity.builder()
                .userId(userId)
                .refreshTokenValue(refreshToken)
                .build();

        loginTokenService.save(loginTokenEntity);

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        boolean isStudent = userEntity.getUserType() == 0;
        boolean isTeacher = userEntity.getUserType() == 1;
        boolean isAdmin = userEntity.getUserType() == 2;

        Map<String, Object> responseBody = new HashMap<>();

//        responseBody.put("authorization", tokens.get("authorization"));
        responseBody.put("refresh", refreshToken);
        responseBody.put("isStudent", isStudent);
        responseBody.put("isTeacher", isTeacher);
        responseBody.put("isAdmin", isAdmin);
        responseBody.put("nickname", userEntity.getNickname());
        responseBody.put("userEmail", userId.getUserEmail());
        responseBody.put("provider", userId.getProvider());
        responseBody.put("profileImageUrl", userEntity.getProfileImageUrl());
        responseBody.put("isLogin", true);

        String logoutUrl = String.format(
                "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=%s&client_secret=%s&access_token=%s&service_provider=%s",
                clientId, clientSecret, accessToken, "NAVER"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseNaver;
        try {
            responseNaver = restTemplate.exchange(logoutUrl, HttpMethod.POST, requestEntity, String.class);
            log.info("Naver logout response: {}", responseNaver.getBody());
        } catch (HttpClientErrorException e) {
            log.error("Naver logout failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Naver logout failed");
        }

        return ResponseEntity.ok(responseBody);
    }

    private Map<String, Object> getNaverUserInfo(String accessToken) {
        String apiUrl = "https://openapi.naver.com/v1/nid/me";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
            log.info(response.getBody().toString());
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && "00".equals(responseBody.get("resultcode"))) {
                return (Map<String, Object>) responseBody.get("response");
            }
            return null;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decodeUnicodeString(String unicode) {
        Writer writer = new StringWriter();
        try {
            for (int i = 0; i < unicode.length(); i++) {
                char c = unicode.charAt(i);
                if (c == '\\' && i + 1 < unicode.length() && unicode.charAt(i + 1) == 'u') {
                    int code = Integer.parseInt(unicode.substring(i + 2, i + 6), 16);
                    writer.write((char) code);
                    i += 5;
                } else {
                    writer.write(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer.toString();
    }
}
