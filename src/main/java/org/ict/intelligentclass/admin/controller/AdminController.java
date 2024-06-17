package org.ict.intelligentclass.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.user.model.dto.AttendanceDto;
import org.ict.intelligentclass.user.model.dto.UserDto;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@CrossOrigin // 리액트 애플리케이션(포트가 다름)의 자원 요청을 처리하기 위함
public class AdminController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> selectAdminByEmail(@RequestParam("email") String email,
                                                      @RequestParam("provider") String provider) {
        log.info("/admins/email/provider/" + email + " 관리자 정보 조회 요청");
        UserDto userDto = userService.getUserById(email, provider);
        if (userDto.getUserType() != 2) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 관리자가 아닌 경우 접근 불가
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<UserDto> selectAdminByNickname(@PathVariable("nickname") String nickname) {
        log.info("/admins/nickname/" + nickname + " 관리자 정보 조회 요청");
        UserDto userDto = userService.getUserByNickname(nickname);
        if (userDto.getUserType() != 2) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 관리자가 아닌 경우 접근 불가
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<UserDto> selectAdminByPhone(@PathVariable("phone") String phone) {
        log.info("/admins/phone/" + phone + " 관리자 정보 조회 요청");
        UserDto userDto = userService.getUserByPhone(phone);
        if (userDto.getUserType() != 2) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN); // 관리자가 아닌 경우 접근 불가
        }
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<UserDto>> selectAdminByName(@PathVariable("name") String name) {
        log.info("/admins/name/" + name + " 관리자 정보 조회 요청");
        List<UserDto> userDtos = userService.getUserByName(name);
        List<UserDto> adminDtos = userDtos.stream()
                .filter(userDto -> userDto.getUserType() == 2)
                .collect(Collectors.toList());
        return new ResponseEntity<>(adminDtos, HttpStatus.OK);
    }

    @GetMapping("/today-registered")
    public ResponseEntity<List<UserDto>> selectTodayRegisteredAdmins() {
        log.info("/admins/today-registered/ 오늘 가입한 관리자 리스트 조회 요청");
        List<UserDto> userDtos = userService.selectTodayRegisteredUsers();
        List<UserDto> adminDtos = userDtos.stream()
                .filter(userDto -> userDto.getUserType() == 2)
                .collect(Collectors.toList());
        return new ResponseEntity<>(adminDtos, HttpStatus.OK);
    }

    @GetMapping("/registered-admins-period")
    public ResponseEntity<List<UserDto>> selectRegisteredAdminsByPeriod(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
                                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        log.info("/admins/registered-admins-period/ 기간 동안 가입한 관리자 리스트 조회 요청");
        List<UserDto> userDtos = userService.selectRegisteredUsersByPeriod(begin, end);
        List<UserDto> adminDtos = userDtos.stream()
                .filter(userDto -> userDto.getUserType() == 2)
                .collect(Collectors.toList());
        return new ResponseEntity<>(adminDtos, HttpStatus.OK);
    }

    @GetMapping("/report/{email}/{provider}")
    public ResponseEntity<Integer> selectReportCount(@PathVariable("email") String email,
                                                     @PathVariable("provider") String provider) {
        log.info("/admins/report/" + email + "/" + provider + " 경고 횟수 조회 요청");
        int reportCount = userService.selectReportCount(email, provider);
        return new ResponseEntity<>(reportCount, HttpStatus.OK);
    }

    @GetMapping("/loginok/{email}/{provider}")
    public ResponseEntity<String> checkLoginOk(@PathVariable("email") String email,
                                               @PathVariable("provider") String provider) {
        log.info("/admins/login-ok/" + email + "/" + provider + " 로그인 제한 여부 조회 요청");
        String loginOk = userService.checkLoginOk(email, provider);
        return new ResponseEntity<>(loginOk, HttpStatus.OK);
    }

    @GetMapping("/faceloginyn/{email}/{provider}")
    public ResponseEntity<String> checkFaceLoginYN(@PathVariable("email") String email,
                                                   @PathVariable("provider") String provider) {
        log.info("/admins/face-login-yn/" + email + "/" + provider + " 얼굴 등록 여부 조회 요청");
        String faceLoginYn = userService.checkFaceLoginYN(email, provider);
        return new ResponseEntity<>(faceLoginYn, HttpStatus.OK);
    }

    @GetMapping("/select-attendance/{email}/{provider}")
    public ResponseEntity<List<AttendanceDto>> selectAttendance(@PathVariable("email") String email,
                                                                @PathVariable("provider") String provider) {
        log.info("/admins/select-attendance/" + email + "/" + provider + " 출석일 조회 요청");
        List<AttendanceDto> attendance = userService.selectAttendance(email, provider);
        return new ResponseEntity<>(attendance, HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<?> insertAdmin(@RequestBody UserDto adminDto) {
        log.info("/admins/admin/" + adminDto.getUserEmail() + "/" + adminDto.getProvider() + " 관리자 회원가입 요청");

        try {
            // 기본 프로필 이미지 URL 설정
            String defaultProfileImageUrl = "/images/defaultProfile.png"; // 실제 기본 프로필 이미지 경로

            // 프로필 이미지 URL이 없는 경우 기본 이미지로 설정
            if (adminDto.getProfileImageUrl() == null || adminDto.getProfileImageUrl().isEmpty()) {
                adminDto.setProfileImageUrl(defaultProfileImageUrl);
            }

            adminDto.setUserType(2); // 관리자 타입 설정
            UserDto createdUser = userService.insertUser(adminDto);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteadmin/{email}/{provider}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable("email") String email,
                                            @PathVariable("provider") String provider,
                                            @RequestParam("reason") String reason) {
        log.info("/admins/deleteadmin/" + email + "/" + provider + " 관리자 삭제 요청");
        userService.deleteUser(email, provider, reason);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/faceloginyn/{email}/{provider}")
    public ResponseEntity<Void> updateFaceLoginYn(@PathVariable("email") String email,
                                                  @PathVariable("provider") String provider) {
        log.info("/admins/" + email + "/" + provider + "/face-login-yn 변경 요청");
        userService.updateFaceLoginYn(email, provider);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/report/{email}/{provider}")
    public ResponseEntity<Void> updateReportCount(@PathVariable("email") String email,
                                                  @PathVariable("provider") String provider) {
        log.info("/admins/" + email + "/" + provider + "/increment-report-count 요청");
        userService.updateReportCount(email, provider);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/admintype/{email}/{provider}")
    public ResponseEntity<Void> updateAdminType(@PathVariable("email") String email,
                                                @PathVariable("provider") String provider,
                                                @RequestParam("adminType") int adminType) {
        log.info("/admins/" + email + "/" + provider + "/admin-type 변경 요청");
        userService.updateUserType(email, provider, adminType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/loginok/{email}/{provider}")
    public ResponseEntity<Void> updateLoginOk(@PathVariable("email") String email,
                                              @PathVariable("provider") String provider,
                                              @RequestParam("loginOk") String loginOk) {
        log.info("/admins/" + email + "/" + provider + "/login-ok 변경 요청");
        userService.updateLoginOk(email, provider, loginOk);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/password/{email}/{provider}")
    public ResponseEntity<Void> updateAdminPwd(@PathVariable("email") String email,
                                               @PathVariable("provider") String provider,
                                               @RequestParam("newPw") String newPw) {
        log.info("/admins/" + email + "/" + provider + "/비밀번호 변경 요청");
        userService.updateUserPwd(email, provider, newPw);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
