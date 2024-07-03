package org.ict.intelligentclass.user.controller;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ict.intelligentclass.career.jpa.entity.CareerEntity;
import org.ict.intelligentclass.career.model.service.CareerService;
import org.ict.intelligentclass.education.jpa.entity.EducationEntity;
import org.ict.intelligentclass.education.model.service.EducationService;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.UserInterestEntity;
import org.ict.intelligentclass.user.model.dto.*;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin     // 리액트 애플리케이션(포트가 다름)의 자원 요청을 처리하기 위함
public class UserController {
    private final UserService userService;
    private final EducationService educationService;
    private final CareerService careerService;
    private final JavaMailSender mailSender;


    // 사용자 정보 조회
    @GetMapping
    public ResponseEntity<UserDto> selectUserByEmail(@RequestParam("userEmail") String userEmail,
                                                     @RequestParam("provider") String provider) {
        log.info("/users : " + userEmail, provider + " 사용자 정보 조회 요청");
        return new ResponseEntity<>(userService.getUserById(userEmail, provider), HttpStatus.OK);
    }

    // 닉네임으로 사용자 정보 조회
    @GetMapping("/nickname/{nickname}")
    public ResponseEntity<UserDto> selectUserByNickname(@PathVariable("nickname") String nickname) {
        log.info("/users/nickname/" + nickname + " 사용자 정보 조회 요청");
        return new ResponseEntity<>(userService.getUserByNickname(nickname), HttpStatus.OK);
    }

    // 사용자 정보 휴대폰으로 조회
    @GetMapping("/phone/{phone}")
    public ResponseEntity<UserDto> selectUserByPhone(@PathVariable("phone") String phone) {
        log.info("/users/phone/" + phone + " 사용자 정보 조회 요청");
        return new ResponseEntity<>(userService.getUserByPhone(phone), HttpStatus.OK);
    }

    // 사용자 정보 리스트 이름으로 조회
    @GetMapping("/name/{name}")
    public ResponseEntity<List<UserDto>> selectUserByName(@PathVariable("name") String name) {
        log.info("/users/name/" + name + " 사용자 정보 조회 요청");
        return new ResponseEntity<>(userService.getUserByName(name), HttpStatus.OK);
    }

    // 이메일 중복 검사 조회
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

    // 닉네임 중복 검사 조회
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

    // 유저 타입 조회
    @GetMapping("/check-usertype/{email}/{provider}")
    public ResponseEntity<UserDto> checkUserType(@PathVariable("email") String email,
                                                 @PathVariable("provider") String provider) {
        log.info("/users/check-usertype/" + email, provider + " 유저 타입 조회 요청");
        return new ResponseEntity<>(userService.checkUserType(email, provider), HttpStatus.OK);
    }

    // 오늘 가입한 사용자 리스트 조회
    @GetMapping("/today-registered")
    public ResponseEntity<List<UserDto>> selectTodayRegisteredUsers() {
        log.info("/users/today-registered/ 오늘 가입한 사용자 리스트 조회 요청");
        return new ResponseEntity<>(userService.selectTodayRegisteredUsers(), HttpStatus.OK);
    }

    // 기간 동안 가입한 사용자 리스트 조회
    @GetMapping("/registered-users-period")
    public ResponseEntity<List<UserDto>> selectRegisteredUsersByPeriod(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date begin,
                                                                       @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date end) {
        log.info("/users/registered-users-period/ 기간 동안 가입한 사용자 리스트 조회 요청");
        List<UserDto> users = userService.selectRegisteredUsersByPeriod(begin, end);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // 경고 횟수 조회
    @GetMapping("/report/{email}/{provider}")
    public ResponseEntity<Integer> selectReportCount(@PathVariable("email") String email,
                                                     @PathVariable("provider") String provider) {
        log.info("/users/report/" + email + "/" + provider + " 경고 횟수 조회 요청");
        int reportCount = userService.selectReportCount(email, provider);
        return new ResponseEntity<>(reportCount, HttpStatus.OK);
    }

    // 로그인 제한 여부 조회
    @GetMapping("/loginok/{email}/{provider}")
    public ResponseEntity<String> checkLoginOk(@PathVariable("email") String email,
                                               @PathVariable("provider") String provider) {
        log.info("/users/login-ok/" + email + "/" + provider + " 로그인 제한 여부 조회 요청");
        String loginOk = userService.checkLoginOk(email, provider);
        return new ResponseEntity<>(loginOk, HttpStatus.OK);
    }

    // 얼굴 등록 여부 조회
    @GetMapping("/faceloginyn/{email}/{provider}")
    public ResponseEntity<String> checkFaceLoginYN(@PathVariable("email") String email,
                                                   @PathVariable("provider") String provider) {
        log.info("/users/face-login-yn/" + email + "/" + provider + " 얼굴 등록 여부 조회 요청");
        String faceLoginYn = userService.checkFaceLoginYN(email, provider);
        return new ResponseEntity<>(faceLoginYn, HttpStatus.OK);
    }

    // 출석일 조회
    @GetMapping("/select-attendance/{email}/{provider}")
    public ResponseEntity<List<AttendanceDto>> selectAttendance(@PathVariable("email") String email,
                                                                @PathVariable("provider") String provider) {
        log.info("/users/select-attendance : " + email + ", " + provider + " 출석일 조회 요청");
        return new ResponseEntity<>(userService.selectAttendance(email, provider), HttpStatus.OK);
    }

    @PostMapping("/check-attendance")
    public ResponseEntity<Void> checkAttendance(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String provider = request.get("provider");

        log.info("/users/check-attendance : " + email + ", " + provider + " 출석일 조회 요청");
        userService.checkAttendance(email, provider);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 이메일 인증코드 전송
    @PostMapping("/send-verification-code")
    public ResponseEntity<Map<String, String>> sendVerificationCode(@RequestParam("userEmail") String userEmail) {
        log.info("/users/send-verification-code : " + userEmail + " 인증 코드 전송 요청");

        Map<String, String> response = new HashMap<>();

        try {
            String verificationCode = createKey();

            log.info("인증코드 : " + verificationCode);

            String subject = "[InTelliClass] 인증번호를 안내해 드립니다.";
            String text = String.format(
                    "<!DOCTYPE html>" +
                            "<html lang='ko'>" +
                            "<head><meta charset='UTF-8'><title>인증번호 안내</title></head>" +
                            "<body style='font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px;'>" +
                            "<div style='max-width: 600px; margin: 0 auto; background-color: white; border: 1px solid #e0e0e0; padding: 20px; border-radius: 8px;'>" +
                            "<h2 style='color: #0073e6; border-bottom: 2px solid #e0e0e0; padding-bottom: 10px;'>[InTelliClass] 인증번호를 안내해 드립니다.</h2>" +
                            "<p>안녕하세요.</p><br>" +
                            "<p>회원님의 계정 보안을 위해 이메일 인증이 필요합니다.<br>" +
                            "아래 인증 번호를 확인하신 후 이메일 인증절차를 완료하세요.</p><br>" +
                            "<table style='width: 100%%; border-collapse: collapse; margin-top: 20px;'>" + // 변경된 부분
                            "<tr><th style='background-color: #f5f5f5; text-align: left; padding: 10px; border: 1px solid #ddd;'>도메인</th><td style='padding: 10px; border: 1px solid #ddd;'>intelliclass.co.kr</td></tr>" +
                            "<tr><th style='background-color: #f5f5f5; text-align: left; padding: 10px; border: 1px solid #ddd;'>신청 내용</th><td style='padding: 10px; border: 1px solid #ddd;'>사용자 이메일 인증</td></tr>" +
                            "<tr><th style='background-color: #f5f5f5; text-align: left; padding: 10px; border: 1px solid #ddd;'>인증 번호</th><td style='padding: 10px; border: 1px solid #ddd;'>%s</td></tr>" +
                            "<tr><th style='background-color: #f5f5f5; text-align: left; padding: 10px; border: 1px solid #ddd;'>요청 일시</th><td style='padding: 10px; border: 1px solid #ddd;'>%s</td></tr>" +
                            "</table>" +
                            "<p>· 위 인증 번호를 입력창에 입력하시기 바랍니다.</p><br><br><hr>" +
                            "<div style='text-align: center;'>" + // 중앙 정렬 시작
                            "<p style='color: #888;'>※ 본 메일은 InTelliClass 서비스 이용에 관한 안내와 공지를 위한 메일입니다.<br>" +
                            "따라서 메일에는 수신 거부 장치가 장착되어 있지 않습니다.</p>" +
                            "<p style='border-top: 1px solid #e0e0e0; padding-top: 10px; font-size: 0.9em;'>" +
                            "회사소개 | 약관 | 개인정보처리방침 | 고객센터<br><hr>" +
                            "(주)InTelliClass 서울특별시 서초구 서초대로77길 41, 4층 401 강의실<br>" +
                            "대표전화 02-739-7235  메일/그룹웨어 02-733-3460 IDC/클라우드/보안 02-739-7235" +
                            "</p>" +
                            "<p style='font-size: 0.8em; color: #888;'>ⓒInTelliClass Inc. All Rights Reserved.</p>" +
                            "</div>" + // 중앙 정렬 끝
                            "</div>" +
                            "</body>" +
                            "</html>",
                    verificationCode, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            );

            MimeMessage m = mailSender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(m, "UTF-8");
            h.setFrom("officialintelliclass@naver.com");
            h.setTo(userEmail);
            h.setSubject(subject);
            h.setText(text, true); // HTML 형식을 사용하려면 두 번째 매개변수를 true로 설정
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

    // 진짜 회원 가입
    @PostMapping("/insertuser")
    public ResponseEntity<?> insertUser(@RequestBody EnrollForm enrollForm) {
        log.info("/users/user/ " + enrollForm.getUserEmail() + "/" + enrollForm.getUserType() + " 유저 회원가입 요청");

        try {
            // 강사 신청 여부 설정
            char teacherApply = enrollForm.getUserType() == 1 ? 'Y' : 'N';

            UserDto userDto = UserDto.builder()
                    .userEmail(enrollForm.getUserEmail())
                    .provider(enrollForm.getProvider())
                    .userName(enrollForm.getUserName())
                    .userPwd(enrollForm.getUserPwd())
                    .phone(enrollForm.getPhone())
                    .nickname(enrollForm.getNickname())
                    .registerTime(LocalDateTime.now())
                    .profileImageUrl(enrollForm.getProfileImageUrl())
                    .userType(0) // 강사는 원래 1이지만 관리자 승인후에 1로 바뀔예정
                    .reportCount(enrollForm.getReportCount())
                    .loginOk(enrollForm.getLoginOk())
                    .faceLoginYn(enrollForm.getFaceLoginYn())
                    .snsAccessToken(enrollForm.getSnsAccessToken())
                    .teacherApply(teacherApply) // 강사 신청 여부 설정
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

            // EducationEntity 저장
            List<EducationEntity> educationEntities = enrollForm.getEducations().stream()
                    .peek(education -> education.setNickname(enrollForm.getNickname()))  // Nickname 설정
                    .collect(Collectors.toList());

            educationService.saveAllEducations(educationEntities);


            // CareerEntity 저장
            List<CareerEntity> careerEntities = enrollForm.getCareers().stream()
                    .peek(career -> career.setNickname(enrollForm.getNickname()))  // Nickname 설정
                    .collect(Collectors.toList());

            careerService.saveAllCareers(careerEntities);

            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 회원 정보 수정
    @PutMapping("/update-profile")
    public ResponseEntity<Void> updateUserProfile(@RequestBody UserDto userDto) {
        log.info("/users/update-profile : {} 회원 정보 수정 요청", userDto.getUserEmail());

        try {
            userService.updateUserProfile(userDto);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error updating user profile:", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 사용자 탈퇴
    @DeleteMapping("/deleteuser/{userEmail}/{provider}")
    public ResponseEntity<Void> deleteUser(@PathVariable("userEmail") String userEmail,
                                           @PathVariable("provider") String provider,
                                           @RequestParam("reason") String reason) {
        log.info("/users/deleteuser : " + userEmail + "/" + provider + " 유저 삭제 요청");
        userService.deleteUser(userEmail, provider, reason);
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

    // 비밀번호 변경(로그인 전)
    @PutMapping("/reset-password/{userEmail}/{userPwd}")
    public ResponseEntity<Void> updateUserPwd(@PathVariable("userEmail") String userEmail,
                                              @PathVariable("userPwd") String userPwd) {
        log.info("/users/reset-password : " + userEmail + " 비밀번호 변경 요청");
        userService.updateUserPwd(userEmail, "intelliclass", userPwd);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // 비밀번호 변경(회원 정보 수정)
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody ChangePasswordForm form) {
        log.info("/users/change-password : 비밀번호 변경 요청");

        try {
            userService.changePassword(form.getUserEmail(), "intelliclass", form.getCurrentPassword(), form.getNewPassword());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error changing password:", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 나의 결제한 강의 리스트 조회
    @GetMapping("/purchased-lectures")
    public ResponseEntity<Page<LecturePackageList>> getPurchasedLectures(
            @RequestParam("email") String userEmail,
            @RequestParam("provider") String provider,
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        log.info("User email: {}, provider: {} - get purchased lectures", userEmail, provider);
        Page<LecturePackageList> purchasedLectures = userService.getPurchasedLectures(userEmail, provider, page, size);
        return new ResponseEntity<>(purchasedLectures, HttpStatus.OK);
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



    //채림
    @GetMapping("/all")
    public ResponseEntity<Page<UserDto>> getReportAllUsers(@RequestParam("page") int page, @RequestParam("size") int size) {
        Page<UserDto> users = userService.getReportAllUsers(page, size);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/restrictlogin")
    public ResponseEntity<UserEntity> restrictLogin(@RequestBody String nickname) {
        UserEntity updatedUser = userService.toggleLoginRestriction(nickname);
        return ResponseEntity.ok(updatedUser);
    }


}