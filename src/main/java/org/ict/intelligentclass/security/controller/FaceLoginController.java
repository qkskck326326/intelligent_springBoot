package org.ict.intelligentclass.security.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.security.entity.LoginTokenEntity;
import org.ict.intelligentclass.security.jwt.util.JwtTokenUtil;
import org.ict.intelligentclass.security.service.LoginTokenService;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/face")
@CrossOrigin
public class FaceLoginController {
    private final UserService userService;
    private final LoginTokenService loginTokenService;
    private final JwtTokenUtil jwtTokenUtil;

    public FaceLoginController(UserService userService, LoginTokenService loginTokenService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.loginTokenService = loginTokenService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> faceLogin(@RequestParam("userEmail") String userEmail, @RequestParam("provider") String provider, HttpServletResponse response) throws IOException {
        Optional<UserEntity> optionalUser = userService.findByUserEmailAndProvider(userEmail, provider);

        if (optionalUser.isEmpty()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(null);
        }

        UserEntity userEntity = optionalUser.get();
        UserId userId = new UserId(userEntity.getUserId().getUserEmail(), userEntity.getUserId().getProvider());

        Long accessExpiredMs = 600000L; // 10 minutes
        String accessToken = jwtTokenUtil.generateToken(userId.getUserEmail(), userId.getProvider(), "access", accessExpiredMs);
        Long refreshExpiredMs = 86400000L; // 24 hours
        String refreshToken = jwtTokenUtil.generateToken(userId.getUserEmail(), userId.getProvider(), "refresh", refreshExpiredMs);

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
        responseBody.put("refresh", refreshToken);
        responseBody.put("isStudent", isStudent);
        responseBody.put("isTeacher", isTeacher);
        responseBody.put("isAdmin", isAdmin);
        responseBody.put("nickname", userEntity.getNickname());
        responseBody.put("userEmail", userId.getUserEmail());
        responseBody.put("provider", userId.getProvider());
        responseBody.put("profileImageUrl", userEntity.getProfileImageUrl());
        responseBody.put("isLogin", true);
        responseBody.put("isSnsUser", false);

        return ResponseEntity.ok(responseBody);
    }
}
