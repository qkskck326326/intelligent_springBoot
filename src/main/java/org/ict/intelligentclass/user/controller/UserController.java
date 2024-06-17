package org.ict.intelligentclass.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ict.intelligentclass.security.jwt.util.JwtTokenUtil;
import org.ict.intelligentclass.security.service.LoginTokenService;
import org.ict.intelligentclass.user.model.dto.AttendanceDto;
import org.ict.intelligentclass.user.model.dto.UserDto;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin     // 리액트 애플리케이션(포트가 다름)의 자원 요청을 처리하기 위함
public class UserController {
    private final UserService userService;
    private final LoginTokenService loginTokenService;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<UserDto> selectUserByEmail(@RequestParam("email") String email,
                                                     @RequestParam("provider") String provider) {
        log.info("/users/email/provider/" + email, provider + " 사용자 정보 조회 요청");
        return new ResponseEntity<>(userService.getUserById(email, provider), HttpStatus.OK);
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<UserDto> selectUserByNickname(@PathVariable("nickname") String nickname) {
        log.info("/users/nickname/" + nickname + " 사용자 정보 조회 요청");
        return new ResponseEntity<>(userService.getUserByNickname(nickname), HttpStatus.OK);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<UserDto> selectUserByPhone(@PathVariable("phone") String phone) {
        log.info("/users/phone/" + phone + " 사용자 정보 조회 요청");
        return new ResponseEntity<>(userService.getUserByPhone(phone), HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<UserDto>> selectUserByName(@PathVariable("name") String name) {
        log.info("/users/name/" + name + " 사용자 정보 조회 요청");
        return new ResponseEntity<>(userService.getUserByName(name), HttpStatus.OK);
    }

    @GetMapping("/check-email/{email}/{provider}")
    public ResponseEntity<String> checkEmailDuplicate(@PathVariable("email") String email,
                                                      @PathVariable("provider") String provider) {
        log.info("/users/check-email/" + email + "/" + provider + " 이메일 중복 검사 요청");

        boolean isDuplicate = userService.checkEmailDuplicate(email, provider);

        if (isDuplicate) {
            return new ResponseEntity<>("이메일 중복입니다.", HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>("사용 가능한 이메일입니다.", HttpStatus.OK);
        }
    }

    @GetMapping("/check-nickname/{nickname}")
    public ResponseEntity<String> checkNicknameDuplicate(@PathVariable("nickname") String nickname) {
        log.info("/users/check-nickname/" + nickname + " 닉네임 중복 검사 요청");

        boolean isDuplicate = userService.checkNicknameDuplicate(nickname);

        if (isDuplicate) {
            return new ResponseEntity<>("닉네임 중복입니다.", HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>("사용 가능한 닉네임입니다.", HttpStatus.OK);
        }
    }

    @GetMapping("/check-usertype/{email}/{provider}")
    public ResponseEntity<UserDto> checkUserType(@PathVariable("email") String email,
                                                 @PathVariable("provider") String provider) {
        log.info("/users/check-usertype/" + email, provider + " 유저 타입 조회 요청");
        return new ResponseEntity<>(userService.checkUserType(email, provider), HttpStatus.OK);
    }

    @GetMapping("/today-registered")
    public ResponseEntity<List<UserDto>> selectTodayRegisteredUsers() {
        log.info("/users/today-registered/ 오늘 가입한 사용자 리스트 조회 요청");
        return new ResponseEntity<>(userService.selectTodayRegisteredUsers(), HttpStatus.OK);
    }

    @GetMapping("/registered-users-period")
    public ResponseEntity<List<UserDto>> selectRegisteredUsersByPeriod(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
                                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        log.info("/users/registered-users-period/ 기간 동안 가입한 사용자 리스트 조회 요청");
        List<UserDto> users = userService.selectRegisteredUsersByPeriod(begin, end);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/report/{email}/{provider}")
    public ResponseEntity<Integer> selectReportCount(@PathVariable("email") String email,
                                                     @PathVariable("provider") String provider) {
        log.info("/users/report/" + email + "/" + provider + " 경고 횟수 조회 요청");
        int reportCount = userService.selectReportCount(email, provider);
        return new ResponseEntity<>(reportCount, HttpStatus.OK);
    }

    @GetMapping("/loginok/{email}/{provider}")
    public ResponseEntity<String> checkLoginOk(@PathVariable("email") String email,
                                               @PathVariable("provider") String provider) {
        log.info("/users/login-ok/" + email + "/" + provider + " 로그인 제한 여부 조회 요청");
        String loginOk = userService.checkLoginOk(email, provider);
        return new ResponseEntity<>(loginOk, HttpStatus.OK);
    }

    @GetMapping("/faceloginyn/{email}/{provider}")
    public ResponseEntity<String> checkFaceLoginYN(@PathVariable("email") String email,
                                                   @PathVariable("provider") String provider) {
        log.info("/users/face-login-yn/" + email + "/" + provider + " 얼굴 등록 여부 조회 요청");
        String faceLoginYn = userService.checkFaceLoginYN(email, provider);
        return new ResponseEntity<>(faceLoginYn, HttpStatus.OK);
    }

    @GetMapping("/select-attendance/{email}/{provider}")
    public ResponseEntity<List<AttendanceDto>> selectAttendance(@PathVariable("email") String email,
                                                                @PathVariable("provider") String provider) {
        log.info("/users/select-attendance/" + email + "/" + provider + " 출석일 조회 요청");
        return new ResponseEntity<>(userService.selectAttendance(email, provider), HttpStatus.OK);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 회원 가입
    @PostMapping("/user")
    public ResponseEntity<?> insertUser(@RequestBody UserDto userDto /*, @RequestParam List<Integer> interestIdList*/) {
        log.info("/users/user/" + userDto.getUserEmail() + "/" + userDto.getProvider() + " 유저 회원가입 요청");

        try {
            // 기본 프로필 이미지 URL 설정
            String defaultProfileImageUrl = "/images/defaultProfile.png"; // 실제 기본 프로필 이미지 경로

            // 프로필 이미지 URL이 없는 경우 기본 이미지로 설정
            if (userDto.getProfileImageUrl() == null || userDto.getProfileImageUrl().isEmpty()) {
                userDto.setProfileImageUrl(defaultProfileImageUrl);
            }

            UserDto createdUser = userService.insertUser(userDto /*, interestIdList*/);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    // 로그인
//    @PostMapping("/auth/login") // 로그인은 보안상 get매핑이 아니고, post매핑을 사용함
//    public ResponseEntity<Map<String, String>> login(@RequestBody UserDto userDto) {
//        UserDto user = userService.getUserById(userDto.getUserEmail(), userDto.getProvider());
//        Map<String, String> token = new HashMap<>();
//
//        if (userService.matches(userDto.getUserPwd(), user.getUserPwd())) {
//            String accessToken = jwtTokenUtil.generateToken(user.getUserEmail() , "access", 3600000L);
//            String refreshToken = jwtTokenUtil.generateToken(user.getUserEmail(), "refresh", 1209600000L);
//            token.put("access_token", accessToken);
//            token.put("refresh_token", refreshToken);
//            loginTokenService.createOrUpdateToken(user.getUserEmail(), user.getProvider(), accessToken, refreshToken);
//        } else {
//            throw new InvalidPasswordException("Invalid password");
//        }
//        return ResponseEntity.ok().body(token);
//    }

    // 사용자 탈퇴
    @DeleteMapping("/deleteuser/{email}/{provider}")
    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email,
                                           @PathVariable("provider") String provider,
                                           @RequestParam("reason") String reason) {
        log.info("/users/deleteuser/" + email + "/" + provider + " 유저 삭제 요청");
        userService.deleteUser(email, provider, reason);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    // 로그아웃
//    @PostMapping("/auth/logout")
//    public ResponseEntity<Void> logout(@RequestBody UserDto userDto) {
//        loginTokenService.deleteTokensByEmail(userDto.getUserEmail(), userDto.getProvider());
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 얼굴 등록 여부 변경
    @PutMapping("/faceloginyn/{email}/{provider}")
    public ResponseEntity<Void> updateFaceLoginYn(@PathVariable("email") String email,
                                                  @PathVariable("provider") String provider) {
        log.info("/users/" + email + "/" + provider + "/face-login-yn 변경 요청");
        userService.updateFaceLoginYn(email, provider);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 경고 횟수 1 증가
    @PutMapping("/report/{email}/{provider}")
    public ResponseEntity<Void> updateReportCount(@PathVariable("email") String email,
                                                  @PathVariable("provider") String provider) {
        log.info("/users/" + email + "/" + provider + "/increment-report-count 요청");
        userService.updateReportCount(email, provider);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 사용자 종류 변경
    @PutMapping("/usertype/{email}/{provider}")
    public ResponseEntity<Void> updateUserType(@PathVariable("email") String email,
                                               @PathVariable("provider") String provider,
                                               @RequestParam("userType") int userType) {
        log.info("/users/" + email + "/" + provider + "/user-type 변경 요청");
        userService.updateUserType(email, provider, userType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 로그인 제한 변경
    @PutMapping("/loginok/{email}/{provider}")
    public ResponseEntity<Void> updateLoginOk(@PathVariable("email") String email,
                                              @PathVariable("provider") String provider,
                                              @RequestParam("loginOk") String loginOk) {
        log.info("/users/" + email + "/" + provider + "/login-ok 변경 요청");
        userService.updateLoginOk(email, provider, loginOk);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 비밀번호 변경
    @PutMapping("/password/{email}/{provider}")
    public ResponseEntity<Void> updateUserPwd(@PathVariable("email") String email,
                                              @PathVariable("provider") String provider,
                                              @RequestParam("newPw") String newPw) {
        log.info("/users/" + email + "/" + provider + "/비밀번호 변경 요청");
        userService.updateUserPwd(email, provider, newPw);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}