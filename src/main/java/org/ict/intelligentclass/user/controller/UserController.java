package org.ict.intelligentclass.user.controller;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.UserInterestEntity;
import org.ict.intelligentclass.user.model.dto.AttendanceDto;
import org.ict.intelligentclass.user.model.dto.UserDto;
import org.ict.intelligentclass.user.model.dto.UserInterestDto;
import org.ict.intelligentclass.user.model.dto.enrollForm;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin     // 리액트 애플리케이션(포트가 다름)의 자원 요청을 처리하기 위함
public class UserController {
    private final UserService userService;
    private final JavaMailSender mailSender;

    @GetMapping
    public ResponseEntity<UserDto> selectUserByEmail(@RequestParam("email") String email,
                                                     @RequestParam("provider") String provider) {
        log.info("/users : " + email, provider + " 사용자 정보 조회 요청");
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

    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmailDuplicate(@RequestParam("userEmail") String userEmail,
                                                      @RequestParam("provider") String provider) {
        log.info("/users/check-email : " + userEmail + "," + provider + " 이메일 중복 검사 요청");
        boolean isDuplicate = userService.checkEmailDuplicate(userEmail, provider);

        if (isDuplicate) {
            return new ResponseEntity<>("이메일 중복입니다.", HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>("사용 가능한 이메일입니다.", HttpStatus.OK);
        }
    }

    @GetMapping("/check-nickname")
    public ResponseEntity<String> checkNicknameDuplicate(@RequestParam("nickname") String nickname) {
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

    @PostMapping("/send-verification-code")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestParam("userEmail") String userEmail) {
        log.info("/users/send-verification-code : " + userEmail + " 인증 코드 전송 요청");

        Map<String, String> response = new HashMap<>();

        try {
            String verificationCode = createKey();

            log.info("회원가입 인증코드 : " + verificationCode);

            String subject = "[INTELLICLASS] 인증 코드 발송 안내";
            String text = String.format(
                    "안녕하세요, INTELLICLASS입니다.\n\n" +
                            "회원님의 계정 보안을 위해 아래의 인증 코드를 입력해 주세요.\n\n" +
                            "[인증 코드: %s]\n\n" +
                            "본 메일은 회원님의 요청에 의해 발송되었습니다.\n\n"  +
                            "만약 본인이 요청하지 않은 경우, 즉시 고객센터로 연락해 주시기 바랍니다.\n\n" +
                            "감사합니다.\n\n" +
                            "INTELLICLASS 드림\n\n" +
                            "------------------------------------------------------------------------------------\n" +
                            "이 메일은 발신 전용입니다. 회신하지 말아 주세요.",
                    verificationCode
            );

            MimeMessage m = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(m, "UTF-8");
            h.setFrom("officialintelliclass@naver.com");
            h.setTo(userEmail);
            h.setSubject(subject);
            h.setText(text);
            mailSender.send(m);

            response.put("verificationCode", verificationCode); // 인증 코드 포함
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Failed to send verification code", e);
            response.put("message", "인증 코드 전송에 실패했습니다."); // 오류 메시지 포함
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 랜덤 인증코드 생성
    private String createKey() throws Exception {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("UserController createKey() exception occur");
            throw new Exception(e.getMessage());
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 간단 회원 가입
    @PostMapping("/user")
    public ResponseEntity<?> simpleInsertUser(@RequestBody UserDto userDto /*, @RequestParam List<Integer> interestIdList*/) {
        log.info("/users/user : " + userDto.getUserEmail() + "/" + userDto.getProvider() + " 유저 회원가입 요청");

        try {
            // 기본 프로필 이미지 URL 설정
            String defaultProfileImageUrl = "/images/defaultProfile.png"; // 실제 기본 프로필 이미지 경로

            // 프로필 이미지 URL이 없는 경우 기본 이미지로 설정
            if (userDto.getProfileImageUrl() == null || userDto.getProfileImageUrl().isEmpty()) {
                userDto.setProfileImageUrl(defaultProfileImageUrl);
            }

            UserDto createdUser = userService.insertSimpleUser(userDto /*, interestIdList*/);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 찐 회원 가입
    @PostMapping("/insertuser")
    public ResponseEntity<?> insertUser(@RequestBody enrollForm enrollForm) {
        log.info("/users/user/ " + enrollForm.getUserEmail() + "/" + enrollForm.getUserType() + " 유저 회원가입 요청");

        try {
            UserDto userDto = UserDto.builder()
                    .userEmail(enrollForm.getUserEmail())
                    .provider(enrollForm.getProvider())
                    .userName(enrollForm.getUserName())
                    .userPwd(enrollForm.getUserPwd())
                    .phone(enrollForm.getPhone())
                    .nickname(enrollForm.getNickname())
                    .registerTime(enrollForm.getRegisterTime())
                    .profileImageUrl(enrollForm.getProfileImageUrl())
                    .userType(enrollForm.getUserType())
                    .reportCount(enrollForm.getReportCount())
                    .loginOk(enrollForm.getLoginOk())
                    .faceLoginYn(enrollForm.getFaceLoginYn())
                    .snsAccessToken(enrollForm.getSnsAccessToken())
                    .build();

            log.info(userDto.toString());

            // UserEntity 생성
            UserEntity userEntity = userService.insertUser(userDto);

            // interests를 UserInterestDto로 변환
            List<UserInterestDto> userInterestDtos = enrollForm.getInterests().stream()
                    .map(interestId -> UserInterestDto.builder()
                            .userEmail(enrollForm.getUserEmail())
                            .provider(enrollForm.getProvider())
                            .subCategoryId(interestId)
                            .build())
                    .collect(Collectors.toList());

            // UserInterestDto를 UserInterestEntity로 변환하고 저장
            List<UserInterestEntity> userInterestEntities = userInterestDtos.stream()
                    .map(dto -> dto.toEntity(userEntity))
                    .collect(Collectors.toList());

            // 저장 로직
            userService.saveAllUserInterests(userInterestEntities);

            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 사용자 탈퇴
    @DeleteMapping("/deleteuser/{email}/{provider}")
    public ResponseEntity<Void> deleteUser(@PathVariable("email") String email,
                                           @PathVariable("provider") String provider,
                                           @RequestParam("reason") String reason) {
        log.info("/users/deleteuser/" + email + "/" + provider + " 유저 삭제 요청");
        userService.deleteUser(email, provider, reason);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


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


    // 허강 여기서부터
    @GetMapping("/getpeople")
    public ResponseEntity<List<UserEntity>> getPeople(@RequestParam("userId") String nickname,
                                                   @RequestParam("userType") int userType,
                                                   @RequestParam("addingOption") String addingOption,
                                                      @RequestParam("page") int page,
                                                      @RequestParam(name="searchQuery", required=false) String searchQuery) {

        log.info("userId: " + nickname + "userType: " + userType + " addingOption: " + addingOption + " page: " + page + " searchQuery: " + searchQuery);
        //이미 있는 챗방 로직 만들어야하나?
        //addingOption 은 teachers면 강사들만, students 면 학생들만, groups면 강사와 학생
        List<UserEntity> people;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            people = userService.getPeopleWithQuery(nickname, userType, addingOption, page, searchQuery);
        } else {
            people = userService.getPeople(nickname, userType, addingOption, page);
        }

        return new ResponseEntity<>(people, HttpStatus.OK);

    }
    // 허강 여기까지
}