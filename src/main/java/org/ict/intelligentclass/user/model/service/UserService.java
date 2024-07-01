package org.ict.intelligentclass.user.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.exception.UserNotFoundException;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;
import org.ict.intelligentclass.lecture_packages.jpa.repository.LecturePackageRepository;
import org.ict.intelligentclass.payment.jpa.entity.CouponEntity;
import org.ict.intelligentclass.payment.jpa.repository.CouponRepositoy;
import org.ict.intelligentclass.payment.jpa.repository.PaymentRepository;
import org.ict.intelligentclass.security.config.SecurityConfig;
import org.ict.intelligentclass.user.jpa.entity.ArchivedUserEntity;
import org.ict.intelligentclass.user.jpa.entity.AttendanceEntity;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.UserInterestEntity;
import org.ict.intelligentclass.user.jpa.entity.id.AttendanceId;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.jpa.repository.ArchivedUserRepository;
import org.ict.intelligentclass.user.jpa.repository.AttendanceRepository;
import org.ict.intelligentclass.user.jpa.repository.UserInterestRepository;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.ict.intelligentclass.user.model.dto.AttendanceDto;
import org.ict.intelligentclass.user.model.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ArchivedUserRepository archivedUserRepository;
    private final UserInterestRepository userInterestRepository;
    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;
    private final CouponRepositoy couponRepositoy;
    private final PaymentRepository paymentRepository;
    private final LecturePackageRepository lecturePackageRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    // 생성자를 통해 PasswordEncoder를 받도록 수정
    private PasswordEncoder getPasswordEncoder() {
        return SecurityConfig.passwordEncoder();
    }


    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public Optional<UserEntity> findByUserId(UserId userId) {
        return userRepository.findByEmailAndProvider(userId.getUserEmail(),userId.getProvider());
    }

    public Optional<UserEntity> findByUserEmailAndProvider(String userEmail, String provider) {
        return userRepository.findByEmailAndProvider(userEmail, provider);
    }

    public UserDto getUserById(String userEmail, String provider) {

        Optional<UserEntity> optionalUser = userRepository.findByEmailAndProvider(userEmail, provider);

        if (optionalUser.isPresent()) {
            UserEntity userEntity =  optionalUser.get();
            return userEntity.toDto();
        } else {
            throw new UserNotFoundException("User not found with email : " + userEmail + " and provider : " + provider);
        }
    }

    public UserDto getUserByNickname(String nickname) {
        Optional<UserEntity> optionalUser = userRepository.findByNickname(nickname);

        if (optionalUser.isPresent()) {
            UserEntity userEntity =  optionalUser.get();
            return userEntity.toDto();
        } else {
            throw new UserNotFoundException("User not found with nickname : " + nickname);
        }
    }


    public UserDto getUserByPhone(String phone) {
        Optional<UserEntity> optionalUser = userRepository.findByPhone(phone);

        if (optionalUser.isPresent()) {
            UserEntity userEntity =  optionalUser.get();
            return userEntity.toDto();
        } else {
            throw new UserNotFoundException("User not found with phone : " + phone);
        }
    }

    public ArrayList<UserDto> getUserByName(String name){
        List<UserEntity> userList = userRepository.findByUserName(name);
        ArrayList<UserDto> list = new ArrayList<>();

        for(UserEntity userentity : userList){
            UserDto userDto = userentity.toDto();
            list.add(userDto);
        }

        return list;
    }

    // 이메일 중복 체크
    public boolean checkEmailDuplicate(String userEmail, String provider) {
        // 현재 회원 테이블에서 확인
        boolean existsInCurrentUsers = userRepository.findByEmailAndProvider(userEmail, provider).isPresent();

        // 현재 회원 테이블에 없다면 탈퇴 회원 테이블에서 확인
        if (!existsInCurrentUsers) {
            existsInCurrentUsers = archivedUserRepository.findByEmailAndProvider(userEmail, provider).isPresent();
        }

        // 회원이 존재하면 중복, 존재하지 않으면 중복이 아님
        return existsInCurrentUsers;
    }

    // 닉네임 중복 체크
    public boolean checkNicknameDuplicate(String nickname) {
        Optional<UserEntity> optionalUser = userRepository.findByNickname(nickname);
        return optionalUser.isPresent();
    }

    public UserDto checkUserType(String email, String provider) {
        UserId userId = new UserId(email, provider);
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            return userEntity.toDto();
        } else {
            throw new UserNotFoundException("User not found with email : " + email + " and provider : " + provider);
        }
    }

    public List<UserDto> selectTodayRegisteredUsers() {
        List<UserEntity> userEntities = userRepository.findTodayRegisteredUsers();
        List<UserDto> userDtos = new ArrayList<>();

        for (UserEntity userEntity : userEntities) {
            userDtos.add(userEntity.toDto());
        }


        return userDtos;
    }

    public List<UserDto> selectRegisteredUsersByPeriod(Date begin, Date end) {
        List<UserEntity> userEntities = userRepository.findRegisteredUsersByPeriod(begin, end);
        List<UserDto> userDtos = new ArrayList<>();

        for (UserEntity userEntity : userEntities) {
            userDtos.add(userEntity.toDto());
        }

        return userDtos;
    }

    public int selectReportCount(String email, String provider) {
        Optional<Integer> reportCount = userRepository.findReportCountByUserId(email, provider);
        if (reportCount.isPresent()) {
            return reportCount.get();
        } else {
            throw new UserNotFoundException("User not found with email : " + email + " and provider : " + provider);
        }
    }

    public String checkLoginOk(String email, String provider) {
        Optional<String> loginOk = userRepository.findLoginOkByUserId(email, provider);
        if (loginOk.isPresent()) {
            return loginOk.get();
        } else {
            throw new UserNotFoundException("User not found with email : " + email + " and provider : " + provider);
        }
    }

    public String checkFaceLoginYN(String email, String provider) {
        Optional<String> faceLoginYn = userRepository.findFaceLoginYnByUserId(email, provider);
        if (faceLoginYn.isPresent()) {
            return faceLoginYn.get();
        } else {
            throw new UserNotFoundException("User not found with email : " + email + " and provider : " + provider);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 간단 회원가입(개발초기)
    @Transactional
    public UserDto insertSimpleUser(UserDto userDto /*, List<Integer> interestIdList */ ) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDto.getUserPwd());
        userDto.setUserPwd(encodedPassword);

        // UserEntity 저장
        UserEntity userEntity = userDto.toEntity();
        userRepository.save(userEntity);

        return userEntity.toDto();
    }

    // 일반 회원가입
    @Transactional
    public UserEntity insertUser(UserDto userDto /*, List<Integer> interestIdList */ ) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDto.getUserPwd());
        userDto.setUserPwd(encodedPassword);

        // UserEntity 저장
        UserEntity userEntity = userDto.toEntity();
        userRepository.save(userEntity);

        // 강사일 경우, 강사 요청 리스트 테이블에 행 추가

        return userEntity;
    }

    @Transactional
    public void saveAllUserInterests(List<UserInterestEntity> userInterestEntities) {
        // UserInterestEntity 저장 로직 예시 (repository 사용)
        userInterestRepository.saveAll(userInterestEntities);
    }

    // sns 로그인 유저 가입 및 정보 변경
    @Transactional
    public void insertSocialLoginUser(UserEntity userEntity) {
        // UserEntity 저장
        userRepository.save(userEntity);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 회원 탈퇴
    public void deleteUser(String email, String provider, String reason) {
        UserId userId = new UserId(email, provider);
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            // ArchiveUserEntity 생성 및 저장
            ArchivedUserEntity archiveUserEntity = new ArchivedUserEntity(userId, userEntity.getUserName(), reason);
            archivedUserRepository.save(archiveUserEntity);

            // SNS별 연결 끊기 요청
            String accessToken = userEntity.getSnsAccessToken();
            if (provider.equalsIgnoreCase("kakao")) {
                unlinkKakao(accessToken);
            } else if (provider.equalsIgnoreCase("naver")) {
                unlinkNaver(accessToken);
            } else if (provider.equalsIgnoreCase("google")) {
                unlinkGoogle(accessToken);
            }

            // 원래 유저 삭제
            userRepository.delete(userEntity);
        } else {
            throw new UserNotFoundException("User not found with email: " + email + " and provider: " + provider);
        }
    }

    private void unlinkKakao(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/unlink";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            // 예외 처리 로직
            System.err.println("Failed to unlink Kakao: " + e.getMessage());
        }
    }

    private void unlinkNaver(String accessToken) {
        String url = "https://nid.naver.com/oauth2.0/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "delete");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("access_token", accessToken);
        params.add("service_provider", "NAVER");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            // 예외 처리 로직
            System.err.println("Failed to unlink Naver: " + e.getMessage());
        }
    }

    private void unlinkGoogle(String accessToken) {
        String url = "https://oauth2.googleapis.com/revoke";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessToken);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        try {
            restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            // 예외 처리 로직
            System.err.println("Failed to unlink Google: " + e.getMessage());
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // 얼굴등록여부 변경
    public void updateFaceLoginYn(String email, String provider) {
        userRepository.updateFaceLoginYn(email, provider);
    }

    // 리포트 횟수 변경
    public void updateReportCount(String email, String provider) {
        userRepository.updateReportCount(email, provider);
    }

    // 유저타입 변경
    public void updateUserType(String email, String provider, int userType) {
        userRepository.updateUserType(email, provider, userType);
    }

    // 로그인여부 변경
    public void updateLoginOk(String email, String provider, String loginOk) {
        userRepository.updateLoginOk(email, provider, loginOk);
    }

    // 회원정보수정
    @Transactional
    public void updateUserProfile(UserDto userDto) {
        UserEntity userEntity = userRepository.findByEmailAndProvider(userDto.getUserEmail(), userDto.getProvider())
                .orElseThrow(() -> new RuntimeException("User not found"));


        userEntity.setPhone(userDto.getPhone());
        userEntity.setProfileImageUrl(userDto.getProfileImageUrl());

        userRepository.save(userEntity);
    }

    // 비밀번호찾기에서 비밀번호 변경
    public void updateUserPwd(String email, String provider, String newPw) {
        String encodedPassword = passwordEncoder.encode(newPw);

        userRepository.updateUserPwd(email, provider, encodedPassword);
    }

    // 회원정보수정에서 비밀번호 변경
    public void changePassword(String userEmail, String provider, String currentPassword, String newPassword) {
        Optional<UserEntity> optionalUser = userRepository.findByEmailAndProvider(userEmail, provider);

        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("User not found with email: " + userEmail + " and provider: " + provider);
        }

        UserEntity userEntity = optionalUser.get();

        if (!passwordEncoder.matches(currentPassword, userEntity.getUserPwd())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        userRepository.updateUserPwd(userEmail, provider, encodedNewPassword);
    }

    // 닉네임 생성(sns로그인 유저 닉네임 생성)
    public String generateNickname(String userName) {
        String nickname;
        boolean exists;

        do {
            String uniqueId = UUID.randomUUID().toString().substring(0, 8); // UUID의 앞 8자리를 사용하여 고유성 보장
            nickname = userName + "#" + uniqueId;
            exists = userRepository.existsByNickname(nickname);
        } while (exists);

        return nickname;
    }

    // 출석 체크
    @Transactional
    public void checkAttendance(String email, String provider) {
        LocalDate today = LocalDate.now();
        AttendanceId attendanceId = new AttendanceId(email, provider, today);
        Optional<AttendanceEntity> attendance = attendanceRepository.findById(attendanceId);

        if (!attendance.isPresent()) {
            // 오늘 출석 기록이 없으면 추가
            AttendanceDto attendanceDto = new AttendanceDto();
            attendanceDto.setUserEmail(email);
            attendanceDto.setProvider(provider);
            attendanceDto.setAttendanceTime(today);

            AttendanceEntity attendanceEntity = attendanceDto.toEntity();
            attendanceRepository.save(attendanceEntity);

            // 쿠폰 발급 조건 확인
            issueCouponIfEligible(email, provider, today, 21, "3주 연속 출석 쿠폰", 20.00);
            issueCouponIfEligible(email, provider, today, 14, "2주 연속 출석 쿠폰", 12.00);
            issueCouponIfEligible(email, provider, today, 7, "1주 연속 출석 쿠폰", 5.00);
        }
    }

    private void issueCouponIfEligible(String email, String provider, LocalDate today, int days, String description, double discount) {
        // 최근 `days` 일의 출석 기록을 조회
        List<AttendanceEntity> recentAttendances = attendanceRepository.findByUser_UserId_UserEmailAndUser_UserId_ProviderAndAttendanceId_AttendanceTimeBetween(
                email, provider, today.minusDays(days - 1), today);

        // `days` 일 연속 출석 여부 확인
        if (recentAttendances.size() == days) {
            // 최근 발급된 해당 기간 쿠폰 확인
            List<CouponEntity> recentCoupons = couponRepositoy.findByUserEmailAndProviderOrderByIssuedDateDesc(email, provider);

            // 해당 쿠폰의 유효 기간을 체크하여 중복 발급 방지
            boolean couponAlreadyIssued = recentCoupons.stream().anyMatch(coupon ->
                    coupon.getCouponDescription().equals(description) &&
                            coupon.getIssuedDate().isAfter(today.minusDays(days))
            );

            if (!couponAlreadyIssued) {
                CouponEntity newCoupon = new CouponEntity();
                newCoupon.setUserEmail(email);
                newCoupon.setProvider(provider);
                newCoupon.setCouponDescription(description);
                newCoupon.setDiscountAmount(discount); // 할인 쿠폰 금액
                newCoupon.setIssuedDate(today);

                log.info("쿠폰발급 : {}, {}, {}, {}, {}", email, provider, description, discount, today);
                couponRepositoy.save(newCoupon);
            }
        }
    }


    // 출석 체크 조회
    public ArrayList<AttendanceDto> selectAttendance(String email, String provider){
        List<AttendanceEntity> attendanceEntitiesList = attendanceRepository.selectAttendance(email, provider);
        ArrayList<AttendanceDto> attendanceDtolist = new ArrayList<>();

        for(AttendanceEntity attendanceEntity : attendanceEntitiesList){
            AttendanceDto attendanceDto = attendanceEntity.toDto();
            attendanceDtolist.add(attendanceDto);
        }

        return attendanceDtolist;
    }

    // 나의 결제한 강의 리스트 조회
    public Page<LecturePackageList> getPurchasedLectures(String userEmail, String provider, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Long> lecturePackageIds = paymentRepository.findLecturePackageIdsByUserEmailAndProvider(userEmail, provider);
        Page<LecturePackageEntity> lecturePackages = lecturePackageRepository.findByLecturePackageIdIn(lecturePackageIds, pageable);

        return lecturePackages.map(this::convertToList);
    }

    private LecturePackageList convertToList(LecturePackageEntity entity) {


        return LecturePackageList.builder()
                .lecturePackageId(entity.getLecturePackageId())
                .nickname(entity.getNickname())
                .title(entity.getTitle())
                .thumbnail(entity.getThumbnail())
                .viewCount(entity.getViewCount())
                .registerDate(entity.getRegisterDate())
                .packageLevel(entity.getPackageLevel())
                .build();
    }



    
    // 허강 추가 시작
    public List<UserEntity> getPeople(String nickname, int userType, String addingOption, int page) {

        Pageable pageable = PageRequest.of(page -1, 50, Sort.by(Sort.Direction.DESC, "userName"));
        Page<UserEntity> people = userRepository.findPeople(nickname, addingOption, pageable);
        return people.getContent();
    }

    public List<UserEntity> getPeopleWithQuery(String nickname, int userType, String addingOption, int page, String searchQuery) {

        PageRequest pageRequest = PageRequest.of(page - 1, 50);
        Page<UserEntity> peoplePage = userRepository.findPeopleWithQuery(nickname, addingOption, searchQuery, pageRequest);
        return peoplePage.getContent();
    }

    public List<String> getAdmins(int userType) {
        return userRepository.findNicknamesByUserType(userType);
    }

    // 허강 추가 끝




    // 시원 추가 시작
    public Map<String, Long> getUserRegistrationStats(LocalDate startDate, LocalDate endDate) {
        List<UserEntity> users = userRepository.findAllByRegisterTimeBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());

        // Create a map to hold the results with default 0 values for all dates in the range
        Map<String, Long> registrationStats = new LinkedHashMap<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            registrationStats.put(date.toString(), 0L);
        }

        // Update the map with actual registration counts
        Map<String, Long> actualStats = users.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getRegisterTime().toLocalDate().toString(),
                        Collectors.counting()
                ));

        actualStats.forEach(registrationStats::put);

        // Sort the map by date in descending order
        return registrationStats.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByKey().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Long> getUserRegistrationStatsByMonth(LocalDate startDate, LocalDate endDate) {
        List<UserEntity> users = userRepository.findAllByRegisterTimeBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay());

        Map<String, Long> registrationStats = new LinkedHashMap<>();
        for (LocalDate date = startDate.withDayOfMonth(1); !date.isAfter(endDate.withDayOfMonth(1)); date = date.plusMonths(1)) {
            registrationStats.put(date.format(DateTimeFormatter.ofPattern("yyyy-MM")), 0L);
        }

        Map<String, Long> actualStats = users.stream()
                .collect(Collectors.groupingBy(
                        user -> user.getRegisterTime().format(DateTimeFormatter.ofPattern("yyyy-MM")),
                        Collectors.counting()
                ));

        actualStats.forEach(registrationStats::put);

        return registrationStats;
    }

    public Page<UserDto> getAllUsers(int page, int size, String searchQuery, String searchValue, Integer userType, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("registerTime").descending());
        LocalDateTime startDateTime = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = (endDate != null) ? endDate.atTime(23, 59, 59) : null;

        log.info("Fetching users from repository with searchQuery={}, searchValue={}, userType={}, startDateTime={}, endDateTime={}",
                searchQuery, searchValue, userType, startDateTime, endDateTime);

        Page<UserEntity> userEntities = userRepository.findAllUsers(searchQuery, searchValue, userType, startDateTime, endDateTime, pageable);

        log.info("Fetched {} users from repository", userEntities.getNumberOfElements());

        return userEntities.map(UserEntity::toDto);
    }
    
    // 시원 추가 끝
}