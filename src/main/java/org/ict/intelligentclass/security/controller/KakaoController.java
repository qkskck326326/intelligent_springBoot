//package com.apocaly.apocaly_path_private.user.controller;
//
//
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.ict.intelligentclass.security.jwt.util.JwtTokenUtil;
//import org.ict.intelligentclass.security.service.LoginTokenService;
//import org.ict.intelligentclass.user.jpa.entity.UserEntity;
//import org.ict.intelligentclass.user.jpa.repository.UserRepository;
//import org.ict.intelligentclass.user.model.service.UserService;
//import org.json.JSONException;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//import org.json.JSONObject;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController // 이 클래스가 웹 요청을 처리하는 컨트롤러임을 나타냅니다.
//@RequestMapping("/auth") // 이 컨트롤러는 "/auth"로 시작하는 URL을 처리합니다.
//@Slf4j // 로그를 찍기 위한 설정입니다.
//public class KakaoController {
//
//    @Value("${kakao.client-id}")
//    private String kakaoClientId; // 카카오 클라이언트 ID를 저장합니다.
//
//    @Value("${kakao.redirect-uri}")
//    private String kakaoRedirectUri; // 카카오 로그인 후 리다이렉트할 URI를 저장합니다.
//
//    @Value("${kakao.redirect-signup-uri}")
//    private String kakaoRedirectSignupUri; // 카카오 회원가입 후 리다이렉트할 URI를 저장합니다.
//
//
//    private final UserService userService; // 사용자 서비스 로직을 처리하는 서비스입니다.
//    private final LoginTokenService loginTokenService; // 리프레시 토큰을 관리하는 서비스입니다.
//    private final JwtTokenUtil jwtTokenUtil; // JWT 토큰을 생성하고 검증하는 유틸리티 클래스입니다.
//
//    public KakaoController(UserService userService, LoginTokenService loginTokenService, JwtTokenUtil jwtTokenUtil) {
//        this.userService = userService;
//        this.loginTokenService = loginTokenService;
//        this.jwtTokenUtil = jwtTokenUtil;
//    }
//
//    @GetMapping("/kakao/callback")
//    public void kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException, JSONException {
//        log.info("code = {}", code); // 카카오로부터 받은 코드를 로그에 출력합니다.
//
//        // 액세스 토큰을 요청하기 위한 URL 및 헤더 설정
//        String tokenUrl = "https://kauth.kakao.com/oauth/token"; // 토큰 요청 URL
//        HttpHeaders tokenHeaders = new HttpHeaders(); // HTTP 헤더를 설정합니다.
//        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 헤더의 콘텐츠 타입을 설정합니다.
//        String tokenRequestBody = "grant_type=authorization_code"
//                + "&client_id=" + kakaoClientId
//                + "&redirect_uri=" + kakaoRedirectUri
//                + "&code=" + code; // 토큰 요청 본문을 설정합니다.
//
//        // 토큰 요청
//        HttpEntity<String> tokenRequestEntity = new HttpEntity<>(tokenRequestBody, tokenHeaders); // 토큰 요청 엔티티를 생성합니다.
//        RestTemplate restTemplate = new RestTemplate(); // HTTP 요청을 보내기 위한 객체를 생성합니다.
//        ResponseEntity<String> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenRequestEntity, String.class); // 토큰 요청을 보냅니다.
//        log.info("token response = {}", tokenResponse.getBody()); // 토큰 응답을 로그에 출력합니다.
//
//        JSONObject tokenJson = new JSONObject(tokenResponse.getBody()); // 응답을 JSON 객체로 변환합니다.
//        String accessToken = tokenJson.getString("access_token"); // 액세스 토큰을 추출합니다.
//        log.info("accessToken = {}", accessToken); // 액세스 토큰을 로그에 출력합니다.
//
//        // 사용자 정보를 요청하기 위한 URL 및 헤더 설정
//        String userInfoUrl = "https://kapi.kakao.com/v2/user/me"; // 사용자 정보 요청 URL
//        HttpHeaders userInfoHeaders = new HttpHeaders(); // HTTP 헤더를 설정합니다.
//        userInfoHeaders.set("Authorization", "Bearer " + accessToken); // 액세스 토큰을 헤더에 설정합니다.
//
//        // 사용자 정보 요청
//        HttpEntity<String> userInfoRequestEntity = new HttpEntity<>(userInfoHeaders); // 사용자 정보 요청 엔티티를 생성합니다.
//        ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequestEntity, String.class); // 사용자 정보 요청을 보냅니다.
//        log.info("user info response = {}", userInfoResponse.getBody()); // 사용자 정보 응답을 로그에 출력합니다.
//
//        JSONObject userJson = new JSONObject(userInfoResponse.getBody()); // 응답을 JSON 객체로 변환합니다.
//        String userEmail = userJson.getJSONObject("kakao_account").has("email") ?
//                userJson.getJSONObject("kakao_account").getString("email") : "이메일 정보가 없습니다."; // 이메일을 추출합니다.
//        log.info("email = {}", userEmail); // 이메일을 로그에 출력합니다.
//
//        String userName = userJson.getJSONObject("kakao_account").has("name") ?
//                userJson.getJSONObject("kakao_account").getString("name") : "이름 정보가 없습니다."; // 이름을 추출합니다.
//        log.info("name = {}", userName); // 이메일을 로그에 출력합니다.
//
//
//        //Optional<User> optionalUser = userRepository.findByEmailAndLoginType(email, "kakao"); // 이메일과 로그인 타입으로 사용자를 찾습니다.
//        Optional<UserEntity> optionalUser = userService.findByUserEmailAndProvider(userEmail, "kakao");
//
//
//        if (optionalUser.isPresent()) { // 사용자가 이미 존재하는 경우
//            UserEntity userEntity = optionalUser.get(); // 사용자를 가져옵니다.
//            //userEntity.setLastLogin(LocalDateTime.now()); // 마지막 로그인 시간을 현재 시간으로 설정합니다.
//            userEntity.setSnsAccessToken(accessToken); // 액세스 토큰을 사용자 정보에 저장합니다.
//            //userRepository.save(user); // 사용자 정보를 저장합니다.
//            userService.
//
//            // JWT 토큰 발급
//            Long accessExpiredMs = 600000L; // 액세스 토큰의 만료 시간을 설정합니다.
//            String accessTokenJwt = JwtTokenUtil.generateToken(userEmail, "access", accessExpiredMs); // 액세스 토큰을 생성합니다.
//            Long refreshExpiredMs = 86400000L; // 리프레시 토큰의 만료 시간을 설정합니다.
//            String refreshTokenJwt = JwtTokenUtil.generateToken(userEmail, "refresh", refreshExpiredMs); // 리프레시 토큰을 생성합니다.
//
//            RefreshToken refreshToken = RefreshToken.builder()
//                    .id(UUID.randomUUID()) // 리프레시 토큰의 ID를 설정합니다.
//                    .status("activated") // 리프레시 토큰의 상태를 설정합니다.
//                    .userAgent(response.getHeader("User-Agent")) // 사용자 에이전트를 설정합니다.
//                    .user(user) // 사용자 정보를 설정합니다.
//                    .tokenValue(refreshTokenJwt) // 리프레시 토큰 값을 설정합니다.
//                    .expiresIn(refreshExpiredMs) // 리프레시 토큰의 만료 시간을 설정합니다.
//                    .build();
//
//            refreshService.save(refreshToken); // 리프레시 토큰을 저장합니다.
//
//            // 로그인 성공 후 URL에 토큰 정보 포함
//            String redirectUrl = String.format("http://localhost:3000/user/success?access=%s&refresh=%s&isAdmin=%s",
//                    accessTokenJwt, refreshTokenJwt, user.getIsAdmin()); // 리다이렉트 URL을 설정합니다.
//
//            response.sendRedirect(redirectUrl); // 사용자를 리다이렉트합니다.
//            log.info("로그인 성공: {}", email); // 로그인을 성공했다는 메시지를 로그에 출력합니다.
//        } else { // 사용자가 존재하지 않는 경우
//            log.info("회원가입 필요: {}", email); // 회원가입이 필요하다는 메시지를 로그에 출력합니다.
//            response.sendRedirect("http://localhost:3000/user"); // 회원가입 페이지로 리다이렉트합니다.
//        }
//    }
//
//    @GetMapping("/kakao/signup/callback")
//    public void kakaoSignup(@RequestParam String code, HttpServletResponse response) throws IOException, JSONException {
//        log.info("code = {}", code); // 카카오로부터 받은 코드를 로그에 출력합니다.
//
//        // 액세스 토큰을 요청하기 위한 URL 및 헤더 설정
//        String tokenUrl = "https://kauth.kakao.com/oauth/token"; // 토큰 요청 URL
//        HttpHeaders tokenHeaders = new HttpHeaders(); // HTTP 헤더를 설정합니다.
//        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // 헤더의 콘텐츠 타입을 설정합니다.
//        String tokenRequestBody = "grant_type=authorization_code"
//                + "&client_id=" + kakaoClientId
//                + "&redirect_uri=" + kakaoRedirectSignupUri
//                + "&code=" + code; // 토큰 요청 본문을 설정합니다.
//
//        // 토큰 요청
//        HttpEntity<String> tokenRequestEntity = new HttpEntity<>(tokenRequestBody, tokenHeaders); // 토큰 요청 엔티티를 생성합니다.
//        RestTemplate restTemplate = new RestTemplate(); // HTTP 요청을 보내기 위한 객체를 생성합니다.
//        ResponseEntity<String> tokenResponse = restTemplate.exchange(tokenUrl, HttpMethod.POST, tokenRequestEntity, String.class); // 토큰 요청을 보냅니다.
//        log.info("token response = {}", tokenResponse.getBody()); // 토큰 응답을 로그에 출력합니다.
//
//        JSONObject tokenJson = new JSONObject(tokenResponse.getBody()); // 응답을 JSON 객체로 변환합니다.
//        String accessToken = tokenJson.getString("access_token"); // 액세스 토큰을 추출합니다.
//        log.info("accessToken = {}", accessToken); // 액세스 토큰을 로그에 출력합니다.
//
//        // 사용자 정보를 요청하기 위한 URL 및 헤더 설정
//        String userInfoUrl = "https://kapi.kakao.com/v2/user/me"; // 사용자 정보 요청 URL
//        HttpHeaders userInfoHeaders = new HttpHeaders(); // HTTP 헤더를 설정합니다.
//        userInfoHeaders.set("Authorization", "Bearer " + accessToken); // 액세스 토큰을 헤더에 설정합니다.
//
//        // 사용자 정보 요청
//        HttpEntity<String> userInfoRequestEntity = new HttpEntity<>(userInfoHeaders); // 사용자 정보 요청 엔티티를 생성합니다.
//        ResponseEntity<String> userInfoResponse = restTemplate.exchange(userInfoUrl, HttpMethod.GET, userInfoRequestEntity, String.class); // 사용자 정보 요청을 보냅니다.
//        log.info("user info response = {}", userInfoResponse.getBody()); // 사용자 정보 응답을 로그에 출력합니다.
//
//        JSONObject userJson = new JSONObject(userInfoResponse.getBody()); // 응답을 JSON 객체로 변환합니다.
//        String email = userJson.getJSONObject("kakao_account").has("email") ?
//                userJson.getJSONObject("kakao_account").getString("email") : "이메일 정보가 없습니다."; // 이메일을 추출합니다.
//        log.info("email = {}", email); // 이메일을 로그에 출력합니다.
//
//        Optional<User> optionalUser = userRepository.findByEmailAndLoginType(email, "kakao"); // 이메일과 로그인 타입으로 사용자를 찾습니다.
//
//        if (optionalUser.isPresent()) { // 사용자가 이미 존재하는 경우
//            log.info("이미 등록된 사용자: {}", email); // 이미 등록된 사용자라는 메시지를 로그에 출력합니다.
//            response.sendRedirect("http://localhost:3000/user"); // 사용자 페이지로 리다이렉트합니다.
//        } else { // 사용자가 존재하지 않는 경우
//            User newUser = new User(); // 새로운 사용자 객체를 생성합니다.
//            newUser.setEmail(email); // 이메일을 설정합니다.
//            newUser.setLoginType("kakao"); // 로그인 타입을 설정합니다.
//            newUser.setCreatedAt(LocalDateTime.now()); // 생성 시간을 현재 시간으로 설정합니다.
//            newUser.setIsDelete(false); // 삭제 여부를 false로 설정합니다.
//            newUser.setIsActivated(false); // 활성화 여부를 false로 설정합니다.
//            newUser.setIsEmailVerified(false); // 이메일 인증 여부를 false로 설정합니다.
//            newUser.setIsAdmin(false); // 관리자 여부를 false로 설정합니다.
//            newUser.setPassword(""); // 비밀번호를 빈 문자열로 설정합니다.
//            userRepository.save(newUser); // 새로운 사용자를 저장합니다.
//            log.info("회원가입 성공: {}", email); // 회원가입 성공 메시지를 로그에 출력합니다.
//
//            // 카카오 로그아웃 처리
//            String logoutUrl = "https://kapi.kakao.com/v1/user/logout"; // 로그아웃 요청 URL
//            HttpHeaders logoutHeaders = new HttpHeaders(); // HTTP 헤더를 설정합니다.
//            logoutHeaders.set("Authorization", "Bearer " + accessToken); // 액세스 토큰을 헤더에 설정합니다.
//
//            HttpEntity<String> logoutRequestEntity = new HttpEntity<>(logoutHeaders); // 로그아웃 요청 엔티티를 생성합니다.
//            ResponseEntity<String> logoutResponse = restTemplate.exchange(logoutUrl, HttpMethod.POST, logoutRequestEntity, String.class); // 로그아웃 요청을 보냅니다.
//            log.info("logout response = {}", logoutResponse.getBody()); // 로그아웃 응답을 로그에 출력합니다.
//
//            response.sendRedirect("http://localhost:3000/user/login"); // 로그인 페이지로 리다이렉트합니다.
//        }
//    }
//}