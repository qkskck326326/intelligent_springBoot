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
@RequestMapping("/google")
@RequiredArgsConstructor
@CrossOrigin
public class GoogleController {
    private final UserService userService;
    private final LoginTokenService loginTokenService;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @PostMapping
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String code = request.get("code");
        String accessToken = "";
        String userEmail = "";
        Map<String, String> userInfoResponse = new HashMap<>();

        if (code == null) {
            log.info("code is missing");
            return ResponseEntity.badRequest().body("code is missing");
        }

        // 구글 API를 호출하여 사용자 정보를 가져옵니다.
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 엑세스 토큰 요청
            Map<String, String> tokenRequest = new HashMap<>();
            tokenRequest.put("code", code);
            tokenRequest.put("client_id", clientId);
            tokenRequest.put("client_secret", clientSecret);
            tokenRequest.put("redirect_uri", redirectUri);
            tokenRequest.put("grant_type", "authorization_code");

            Map<String, String> tokenResponse = restTemplate.postForObject("https://oauth2.googleapis.com/token", tokenRequest, Map.class);
            accessToken = tokenResponse.get("access_token");

            // 사용자 정보 요청
            userInfoResponse = restTemplate.getForObject("https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken, Map.class);
            userEmail = userInfoResponse.get("email");

            log.info("userInfoResponse" + userInfoResponse.toString());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("구글 인증 실패: " + e.getMessage());
        }


        Optional<UserEntity> optionalUser = userService.findByUserEmailAndProvider(userEmail, "google");


        if (optionalUser.isPresent()) { // 유저가 이미 존재하면 로그인 처리
            UserEntity userEntity = optionalUser.get();
            userEntity.setSnsAccessToken(accessToken);
            userService.insertSocialLoginUser(userEntity);

            return handleLogin(userEntity, response);
        } else { // 유저가 존재하지 않으면 회원가입 처리

            return handleSignUp(userInfoResponse, accessToken);
        }
    }

    private ResponseEntity<?> handleSignUp(Map<String, String> userInfo, String accessToken) {
        //String userName = decodeUnicodeString((String) userInfo.get("name"));

        UserDto userDto = new UserDto();
        userDto.setUserEmail((String) userInfo.get("email"));
        userDto.setProvider("google");
        userDto.setUserName(userInfo.get("name"));
        userDto.setUserPwd("");
        userDto.setPhone("");
        userDto.setNickname("");
        userDto.setRegisterTime(LocalDateTime.now());
        userDto.setProfileImageUrl((String) userInfo.get("picture"));
        userDto.setUserType(0);
        userDto.setReportCount(0);
        userDto.setLoginOk('Y');
        userDto.setFaceLoginYn('N');
        userDto.setSnsAccessToken(accessToken);

        userService.insertSocialLoginUser(userDto.toEntity());
        log.info("회원가입 성공: {}, {}", userDto.getUserEmail(), userDto.getProvider());


//        String logoutUrl = "https://accounts.google.com/o/oauth2/revoke?token=" + userDto.getSnsAccessToken();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> responseGoogle;
//        try {
//            responseGoogle = restTemplate.exchange(logoutUrl, HttpMethod.POST, requestEntity, String.class);
//            log.info("Google logout response: {}", responseGoogle.getBody());
//        } catch (HttpClientErrorException e) {
//            log.error("Google logout failed: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Google logout failed");
//        }


        return ResponseEntity.ok("User registered successfully");
    }

    private ResponseEntity<?> handleLogin(UserEntity userEntity, HttpServletResponse response) {

        UserId userId = userEntity.getUserId();

        Long accessExpiredMs = 600000L;
        String accessToken = jwtTokenUtil.generateToken(userId.getUserEmail(), "google", "access", accessExpiredMs);
        Long refreshExpiredMs = 86400000L;
        String refreshToken = jwtTokenUtil.generateToken(userId.getUserEmail(), "google", "refresh", refreshExpiredMs);

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


//        String logoutUrl = "https://accounts.google.com/o/oauth2/revoke?token=" + userEntity.getSnsAccessToken();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> responseGoogle;
//        try {
//            responseGoogle = restTemplate.exchange(logoutUrl, HttpMethod.POST, requestEntity, String.class);
//            log.info("Google logout response: {}", responseGoogle.getBody());
//        } catch (HttpClientErrorException e) {
//            log.error("Google logout failed: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Google logout failed");
//        }

        return ResponseEntity.ok(responseBody);
    }

}
