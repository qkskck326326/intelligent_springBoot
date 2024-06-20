package org.ict.intelligentclass.security.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.security.entity.LoginTokenEntity;
import org.ict.intelligentclass.security.jwt.util.JwtTokenUtil;
import org.ict.intelligentclass.security.service.LoginTokenService;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.model.dto.UserDto;
import org.ict.intelligentclass.user.model.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/kakao")
@CrossOrigin
public class KakaoController {

    @Value("${kakao.client-id}")
    private String kakaoClientId;

    @Value("${kakao.redirect-uri}")
    private String kakaoRedirectUri;

//    @Value("${kakao.redirect-signup-uri}")
//    private String kakaoRedirectSignupUri;

    private final UserService userService;
    private final LoginTokenService loginTokenService;
    private final JwtTokenUtil jwtTokenUtil;

    public KakaoController(UserService userService, LoginTokenService loginTokenService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.loginTokenService = loginTokenService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/callback")
    public void kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException, JSONException {
        log.info("code = {}", code);

        String tokenUrl = "https://kauth.kakao.com/oauth/token";
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String tokenRequestBody = "grant_type=authorization_code"
                + "&client_id=" + kakaoClientId
                + "&redirect_uri=" + kakaoRedirectUri
                + "&code=" + code;

        HttpEntity<String> tokenRequestEntity = new HttpEntity<>(tokenRequestBody, tokenHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenRequestEntity, String.class);
        log.info("token response = {}", tokenResponse.getBody());

        JSONObject tokenJson = new JSONObject(tokenResponse.getBody());
        String kakaoAccessToken = tokenJson.getString("access_token");
        log.info("accessToken = {}", kakaoAccessToken);

        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.set("Authorization", "Bearer " + kakaoAccessToken);

        HttpEntity<String> userInfoRequestEntity = new HttpEntity<>(userInfoHeaders);
        ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequestEntity, String.class);
        log.info("user info response = {}", userInfoResponse.getBody());

        JSONObject userJson = new JSONObject(userInfoResponse.getBody());
        String userEmail = userJson.getJSONObject("kakao_account").has("email") ?
                userJson.getJSONObject("kakao_account").getString("email") : "이메일 정보가 없습니다.";
        log.info("email = {}", userEmail);

        Optional<UserEntity> optionalUser = userService.findByUserEmailAndProvider(userEmail, "kakao");

        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            userEntity.setSnsAccessToken(kakaoAccessToken);
            UserId userId = userEntity.getUserId();
            userService.insertSocialLoginUser(userEntity);

            Long accessExpiredMs = 600000L;
            String accessToken = jwtTokenUtil.generateToken(userId.getUserEmail(), "kakao", "access", accessExpiredMs);
            Long refreshExpiredMs = 86400000L;
            String refreshToken = jwtTokenUtil.generateToken(userId.getUserEmail(), "kakao", "refresh", refreshExpiredMs);

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

            String redirectUrl = String.format(
                    "http://localhost:3000/user/login?access=%s&refresh=%s&userEmail=%s&provider=%s&nickname=%s&profileImageUrl=%s&isStudent=%s&isTeacher=%s&isAdmin=%s",
                    accessToken, refreshToken, userEntity.getUserId().getUserEmail(), userEntity.getUserId().getProvider(),
                    userEntity.getNickname(), userEntity.getProfileImageUrl(), isStudent, isTeacher, isAdmin
            );

            log.info("redirectUrl: " + redirectUrl);

            response.sendRedirect(redirectUrl);
        } else {
            handleKakaoSignup(userJson, response, kakaoAccessToken);
        }
    }

//    private void successfulAuthentication(HttpServletResponse response, UserEntity userEntity, String accessToken, String refreshToken) throws IOException {
//        response.addHeader("Authorization", "Bearer " + accessToken);
//        response.setHeader("Access-Control-Expose-Headers", "Authorization");
//
//        boolean isStudent = userEntity.getUserType() == 0;
//        boolean isTeacher = userEntity.getUserType() == 1;
//        boolean isAdmin = userEntity.getUserType() == 2;
//
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("userEmail", userEntity.getUserId().getUserEmail());
//        responseBody.put("provider", userEntity.getUserId().getProvider());
//        responseBody.put("nickname", userEntity.getNickname());
//        responseBody.put("profileImageUrl", userEntity.getProfileImageUrl());
//        responseBody.put("isStudent", isStudent);
//        responseBody.put("isTeacher", isTeacher);
//        responseBody.put("isAdmin", isAdmin);
//        responseBody.put("refresh", refreshToken);
//
//        String responseBodyJson = new ObjectMapper().writeValueAsString(responseBody);
//        response.setContentType("application/json");
//        response.getWriter().write(responseBodyJson);
//        response.getWriter().flush();
//        response.sendRedirect("http://localhost:3000/user/login");
//    }

//    @GetMapping("/signup/callback")
//    public void kakaoSignup(@RequestParam String code, HttpServletResponse response) throws IOException, JSONException {
//        log.info("code = {}", code);
//
//        String tokenUrl = "https://kauth.kakao.com/oauth/token";
//        HttpHeaders tokenHeaders = new HttpHeaders();
//        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        String tokenRequestBody = "grant_type=authorization_code"
//                + "&client_id=" + kakaoClientId
//                + "&redirect_uri=" + kakaoRedirectSignupUri
//                + "&code=" + code;
//
//        HttpEntity<String> tokenRequestEntity = new HttpEntity<>(tokenRequestBody, tokenHeaders);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenRequestEntity, String.class);
//        log.info("token response = {}", tokenResponse.getBody());
//
//        JSONObject tokenJson = new JSONObject(tokenResponse.getBody());
//        String accessToken = tokenJson.getString("access_token");
//        log.info("accessToken = {}", accessToken);
//
//        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
//        HttpHeaders userInfoHeaders = new HttpHeaders();
//        userInfoHeaders.set("Authorization", "Bearer " + accessToken);
//
//        HttpEntity<String> userInfoRequestEntity = new HttpEntity<>(userInfoHeaders);
//        ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequestEntity, String.class);
//        log.info("user info response = {}", userInfoResponse.getBody());
//
//        JSONObject userJson = new JSONObject(userInfoResponse.getBody());
//        handleKakaoSignup(userJson, response, accessToken);
//    }

    private void handleKakaoSignup(JSONObject userJson, HttpServletResponse response, String kakaoAccessToken) throws IOException, JSONException {
        String userEmail = userJson.getJSONObject("kakao_account").has("email") ?
                userJson.getJSONObject("kakao_account").getString("email") : "이메일 정보가 없습니다.";
        log.info("userEmail = {}", userEmail);

        String userName = userJson.getJSONObject("kakao_account").has("name") ?
                userJson.getJSONObject("kakao_account").getString("name") : "이름 정보가 없습니다.";
        log.info("name = {}", userName);

        String phone = userJson.getJSONObject("kakao_account").has("phone_number") ?
                userJson.getJSONObject("kakao_account").getString("phone_number") : "휴대폰 번호 정보가 없습니다.";
        log.info("phone before= {}", phone);

        if (phone.startsWith("+82 ")) {
            phone = "0" + phone.substring(4); // "+82 "를 "0"으로 바꿈
        }
        log.info("phone after= {}", phone);

        String profileImageUrl = userJson.getJSONObject("kakao_account").getJSONObject("profile").has("profile_image_url") ?
                userJson.getJSONObject("kakao_account").getJSONObject("profile").getString("profile_image_url") : "https://intelliclassbucket.s3.ap-northeast-2.amazonaws.com/ProfileImages/defaultProfile.png";
        log.info("profileImageUrl = {}", profileImageUrl);

        Optional<UserEntity> optionalUser = userService.findByUserEmailAndProvider(userEmail, "kakao");

        if (optionalUser.isPresent()) {
            log.info("이미 등록된 사용자: {}", userEmail);
            response.sendRedirect("http://localhost:3000/user/login");
        } else {
            UserDto userDto = new UserDto();
            userDto.setUserEmail(userEmail);
            userDto.setProvider("kakao");
            userDto.setUserName(userName);
            userDto.setUserPwd("");
            userDto.setPhone(phone);
            userDto.setNickname("");
            userDto.setRegisterTime(LocalDateTime.now());
            userDto.setProfileImageUrl(profileImageUrl);
            userDto.setUserType(0);
            userDto.setReportCount(0);
            userDto.setLoginOk('Y');
            userDto.setFaceLoginYn('N');
            userDto.setSnsAccessToken(kakaoAccessToken);

            userService.insertSocialLoginUser(userDto.toEntity());
            log.info("회원가입 성공: {}, {}}", userDto.getUserEmail(), userDto.getProvider());

            String logoutUrl = "https://kapi.kakao.com/v1/user/logout";
            HttpHeaders logoutHeaders = new HttpHeaders();
            logoutHeaders.set("Authorization", "Bearer " + kakaoAccessToken);  // kakaoAccessToken 사용

            HttpEntity<String> logoutRequestEntity = new HttpEntity<>(logoutHeaders);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> logoutResponse = restTemplate.exchange(logoutUrl, HttpMethod.POST, logoutRequestEntity, String.class);
            log.info("logout response = {}", logoutResponse.getBody());

            response.sendRedirect("http://localhost:3000/user/login");
        }
    }
}
