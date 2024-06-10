package org.ict.intelligentclass.user.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.exception.UserNotFoundException;
import org.ict.intelligentclass.security.config.SecurityConfig;
import org.ict.intelligentclass.user.jpa.entity.ArchivedUserEntity;
import org.ict.intelligentclass.user.jpa.entity.AttendanceEntity;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.jpa.repository.ArchivedUserRepository;
import org.ict.intelligentclass.user.jpa.repository.AttendanceRepository;
import org.ict.intelligentclass.user.jpa.repository.UserInterestRepository;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.ict.intelligentclass.user.model.dto.AttendanceDto;
import org.ict.intelligentclass.user.model.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ArchivedUserRepository archivedUserRepository;
    private final UserInterestRepository userInterestRepository;
    private final AttendanceRepository attendanceRepository;
    private final PasswordEncoder passwordEncoder;  // 주입받지 않도록 수정

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

    public boolean checkEmailDuplicate(String userEmail, String provider) {
        UserId userId = new UserId(userEmail, provider);
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        return optionalUser.isPresent();
    }

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

    public ArrayList<AttendanceDto> selectAttendance(String email, String provider){
        List<AttendanceEntity> attendanceEntitiesList = attendanceRepository.selectAttendance(email, provider);
        ArrayList<AttendanceDto> attendanceDtolist = new ArrayList<>();

        for(AttendanceEntity attendanceEntity : attendanceEntitiesList){
            AttendanceDto attendanceDto = attendanceEntity.toDto();
            attendanceDtolist.add(attendanceDto);
        }

        return attendanceDtolist;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public UserDto insertUser(UserDto userDto /*, List<Integer> interestIdList */ ) {
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(userDto.getUserPwd());
        userDto.setUserPwd(encodedPassword);

        // UserEntity 저장
        UserEntity userEntity = userDto.toEntity();
        userRepository.save(userEntity);



        // UserInterestEntity 저장
//        List<UserInterestEntity> userInterests = interestIdList.stream()
//                .map(subCategoryId -> UserInterestEntity.builder()
//                        .interestId(new UserInterestId(userDto.getUserEmail(), userDto.getProvider(), subCategoryId))
//                        .user(userEntity)
//                        .build())
//                .collect(Collectors.toList());
//        userInterestRepository.saveAll(userInterests);

        return userEntity.toDto();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteUser(String email, String provider, String reason) {
        UserId userId = new UserId(email, provider);
        Optional<UserEntity> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            UserEntity userEntity = optionalUser.get();
            // ArchiveUserEntity 생성 및 저장
            ArchivedUserEntity archiveUserEntity = new ArchivedUserEntity(userId, userEntity.getUserName(), reason);
            archivedUserRepository.save(archiveUserEntity);
            // 원래 유저 삭제
            userRepository.delete(userEntity);
        } else {
            throw new UserNotFoundException("User not found with email: " + email + " and provider: " + provider);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void updateFaceLoginYn(String email, String provider) {
        userRepository.updateFaceLoginYn(email, provider);
    }

    public void updateReportCount(String email, String provider) {
        userRepository.updateReportCount(email, provider);
    }

    public void updateUserType(String email, String provider, int userType) {
        userRepository.updateUserType(email, provider, userType);
    }

    public void updateLoginOk(String email, String provider, String loginOk) {
        userRepository.updateLoginOk(email, provider, loginOk);
    }

    public void updateUserPwd(String email, String provider, String newPw) {
        String encodedPassword = passwordEncoder.encode(newPw);

        userRepository.updateUserPwd(email, provider, encodedPassword);
    }






//    public UserDto authenticate(String email, String rawPassword) {
//        String provider = "intelliclass";  // 일반 로그인 유저는 항상 "intelliclass"로 설정
//        Optional<UserEntity> optionalUser = userRepository.findByEmailAndProvider(email, provider);
//        if (optionalUser.isPresent()) {
//            UserEntity userEntity = optionalUser.get();
//            if (passwordEncoder.matches(rawPassword, userEntity.getUserPwd())) {
//                // 출석 체크
//                UserId userId = new UserId(email, provider);
//                AttendanceEntity attendanceEntity = AttendanceEntity.builder()
//                        .id(userId)
//                        .user(userEntity)
//                        .attendanceTime(new Date())
//                        .build();
//                attendanceRepository.save(attendanceEntity);
//
//                return userEntity.toDto();
//            } else {
//                throw new InvalidPasswordException("Invalid password");
//            }
//        } else {
//            throw new UserNotFoundException("User not found with email: " + email + " and provider: " + provider);
//        }
//    }

}