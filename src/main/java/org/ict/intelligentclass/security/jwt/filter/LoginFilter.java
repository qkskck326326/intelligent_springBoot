package org.ict.intelligentclass.security.jwt.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.security.dto.CustomUserDetails;
import org.ict.intelligentclass.security.dto.InputUser;
import org.ict.intelligentclass.security.entity.LoginTokenEntity;
import org.ict.intelligentclass.security.jwt.util.JwtTokenUtil;
import org.ict.intelligentclass.security.service.LoginTokenService;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    // 사용자 인증 및 토큰 발급 역할
    // 로그인 요청을 처리하고 JWT 토큰을 생성하여 응답하는 역할
    private final Long accessExpiredMs; // access Token의 만료일(ms)
    private final Long refreshExpiredMs; // refersh Token의 만료일(ms)

    private final UserService userService;
    private final LoginTokenService loginTokenService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    // 생성자를 통한 필드 초기화 (의존성 주입)
    public LoginFilter(UserService userService, LoginTokenService loginTokenService,
                       AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        // AuthenticationManager를 주입하여 인증처리 가능해짐.
        this.userService = userService;
        this.loginTokenService = loginTokenService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.accessExpiredMs = 600000L; // 10분
        this.refreshExpiredMs = 86400000L; // 1일
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 요청 본문에서 사용자의 로그인 데이터를 InputUser 객체로 변환합니다.
            InputUser loginData = new ObjectMapper().readValue(request.getInputStream(), InputUser.class);
            // 사용자 이름과 비밀번호를 기반으로 AuthenticationToken을 생성합니다.
            // 이 토큰은 사용자가 제공한 이메일과 비밀번호를 담고 있으며, 이후 인증 과정에서 사용됩니다.
            log.info("Attempting to authenticate: " + loginData);
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                    loginData.getUserEmail(), loginData.getUserPwd());

            String emailProvider = loginData.getUserEmail() + "," + loginData.getProvider();
            log.info("Attempting to authenticate with token: 프로바이더 값 확인 : " + loginData.getProvider());
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                    loginData.getUserEmail(), loginData.getUserPwd());
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    emailProvider, loginData.getUserPwd());

            //authToken.setDetails(loginData.getProvider());

            log.info("Attempting to authenticate with token: " + authToken);

            return authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            // 요청 본문을 읽는 과정에서 오류가 발생한 경우, AuthenticationServiceException을 던집니다.
            throw new AuthenticationServiceException("인증 처리 중 오류가 발생했습니다.", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // 로그인 성공 시 실행되는 메소드입니다. 인증된 사용자 정보를 바탕으로 JWT를 생성하고, 이를 응답 헤더에 추가합니다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException {
        // 인증 객체에서 CustomUserDetails를 추출합니다.
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("successfulAuthentication customUserDetails : " + customUserDetails);
        // CustomUserDetails에서 사용자 이름(이메일, 아이디)을 추출합니다.
        String userEmail = customUserDetails.getUsername();
        String provider = customUserDetails.getProvider();
        String nickname = customUserDetails.getNickname();
        String profileImageUrl = customUserDetails.getProfileImageUrl();
        UserId userId = new UserId(userEmail, provider);

//        log.info("successfulAuthentication userEmail : " + userEmail);
//        log.info("successfulAuthentication provider : " + provider);
//        log.info("successfulAuthentication nickname : " + nickname);
//        log.info("successfulAuthentication userId : " + userId);
        // 사용자 이름을 사용하여 JWT를 생성합니다.
        String accessToken  = jwtTokenUtil.generateToken(userEmail, provider,"access", accessExpiredMs);
        String refreshToken  = jwtTokenUtil.generateToken(userEmail, provider,"refresh", refreshExpiredMs);
        Optional<UserEntity> userOptional = userService.findByUserId(userId);  // 이메일(아이디)를 통해 사용자를 조회함.
//        log.info("successfulAuthentication access : " + access);
//        log.info("successfulAuthentication refresh : " + refresh);

        if(userOptional.isPresent()){  // 현재 사용자가 존재하는지 확인.

            LoginTokenEntity loginTokenEntity = LoginTokenEntity.builder() // 사용자가 존재하면 User 객체를 가져와 RefreshToken 엔티티를 생성
                    .userId(userId)
                    .refreshTokenValue(refreshToken)
                    .build();

            loginTokenService.save(loginTokenEntity);  // Refresh Token을 데이터베이스에 저장함.
        }


        // 응답 헤더에 JWT를 'Bearer' 토큰으로 추가합니다. -> 응답 헤더에 Access Token을 추가.
        response.addHeader("Authorization", "Bearer " + accessToken);

        // 클라이언트가 Authorization 헤더를 읽을 수 있도록, 해당 헤더를 노출시킵니다.
        response.setHeader("Access-Control-Expose-Headers", "Authorization");

        // 여기서 부터 사용자 정보를 응답 바디에 추가하는 코드입니다.
        // 사용자의 권한이나 추가 정보를 JSON 형태로 변환하여 응답 바디에 포함시킬 수 있습니다.
        boolean isStudent = customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_STUDENT"));
        boolean isTeacher = customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_TEACHER"));
        boolean isAdmin = customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        // 사용자가 "ROLE_ADMIN" 권한을 가지고 있는지 확인하고 변수 isAdmin에 저장함.

        Map<String, Object> responseBody = new HashMap<>();

        responseBody.put("refresh", refreshToken);  // refresh토큰
        responseBody.put("userEmail", userEmail);
        responseBody.put("provider", provider);
        responseBody.put("nickname", nickname);
        responseBody.put("profileImageUrl",profileImageUrl);
        responseBody.put("isStudent", isStudent);
        responseBody.put("isTeacher", isTeacher);
        responseBody.put("isAdmin", isAdmin);

        // ObjectMapper를 사용하여 Map을 JSON 문자열로 변환합니다.
        String responseBodyJson = new ObjectMapper().writeValueAsString(responseBody);

        // 응답 컨텐츠 타입을 설정합니다.
        response.setContentType("application/json");

        // 응답 바디에 JSON 문자열을 작성합니다.
        response.getWriter().write(responseBodyJson);
        response.getWriter().flush();  // 출력버퍼를 flush함.
    }

    // 로그인 실패 시 실행되는 메소드입니다. 실패한 경우, HTTP 상태 코드 401을 반환합니다.
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) {

        log.info("unsuccessfulAuthentication");
        // failed 객체로부터 최종 원인 예외를 찾습니다.
        Throwable rootCause = failed;  // Throwable : Java의 모든 에러와 예외의 최상위 클래스임. 두 가지 주요 하위 클래스인 Error와 Exception을 포함함.
        while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
            rootCause = rootCause.getCause();
        }

        // rootCause를 기반으로 오류 메시지를 설정합니다.
        String message;
        if (rootCause instanceof UsernameNotFoundException) {
            message = "존재하지 않는 이메일입니다.";
        } else if (rootCause instanceof BadCredentialsException) {
            message = "잘못된 비밀번호입니다.";
        } else if (rootCause instanceof DisabledException) {
            message = "탈퇴한 회원입니다.";
        } else if (rootCause instanceof LockedException) {
            message = "계정이 잠겨 있습니다.";
        } else {
            // 다른 예외들을 처리
            message = "인증에 실패했습니다.";
        }

        // 응답 데이터를 준비합니다.
        Map<String, Object> responseData = new HashMap<>();
        response.setContentType("application/json;charset=UTF-8"); // 응답의 콘텐츠타입.
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.BAD_REQUEST.value());  // 응답의 상태코드는 400(BAD REQUEST)로 설정함.

        // responseData에 타임스탬프, 상태 코드, 오류, 메시지, 요청 경로를 추가함.
        responseData.put("timestamp", LocalDateTime.now().toString());
        responseData.put("status", HttpStatus.BAD_REQUEST.value());
        responseData.put("error", "Unauthorized");
        responseData.put("message", message);
        responseData.put("path", request.getRequestURI());

        // 응답을 보냅니다. //ObjectMapper를 사용하여 responseData를 JSON 문자열로 변환함.
        try {
            String jsonResponse = new ObjectMapper().writeValueAsString(responseData);
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
        } catch (IOException ignored) {
        }
    }
}