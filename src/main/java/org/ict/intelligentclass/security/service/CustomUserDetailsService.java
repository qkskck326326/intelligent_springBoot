package org.ict.intelligentclass.security.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.security.dto.CustomUserDetails;
import org.ict.intelligentclass.security.dto.InputUser;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.repository.ArchivedUserRepository;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

// 스프링 컨테이너에 의해 관리되는 서비스 컴포넌트로 선언합니다.
@Service
@Slf4j
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // 사용자 정보를 조회하기 위한 UserRepository 인터페이스입니다.
    private final ArchivedUserRepository archivedUserRepository;


    // UserDetailsService 인터페이스의 메서드를 구현합니다. 사용자 이름을 기반으로 사용자 정보를 로드합니다.
    @Override
    public UserDetails loadUserByUsername(String emailProvider) {

        // 이메일과 provider를 분리
        String[] parts = emailProvider.split(",", 2);
        String userEmail = parts[0];
        String provider = parts.length > 1 ? parts[1] : null;

        log.info("Parsed email: " + userEmail);
        log.info("Parsed provider: " + provider);

        // 사용자 인증을 위한 사용자 검증 로직을 실행합니다.
        UserEntity userData = validateUser(new InputUser(userEmail, provider));
        log.info("CustomUserDetailsService loadUserByUsername : " + userData.getUserId().getUserEmail());
        log.info("CustomUserDetailsService loadUserByUsername : " +userData.getUserId().getProvider());
        log.info("CustomUserDetailsService loadUserByUsername : " +userData.getUserPwd());


        // CustomUserDetails 객체를 생성하여 반환합니다. 이 객체는 Spring Security에 의해 사용자 인증 과정에서 사용됩니다.
        return new CustomUserDetails(userData);
    }

//    // UserDetailsService 인터페이스의 메서드를 구현합니다. 사용자 이름을 기반으로 사용자 정보를 로드합니다.
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        throw new UsernameNotFoundException("Provider가 없는 사용자 조회는 지원되지 않습니다.");
//    }
//
//    public UserDetails loadUserByUsername(String email, String provider) throws UsernameNotFoundException {
//        UserEntity userEntity = validateUser(new InputUser(email, provider));
//        log.info("CustomUserDetailsService loadUserByUsername : " + userEntity.getUserId().getUserEmail());
//        log.info("CustomUserDetailsService loadUserByUsername : " +userEntity.getUserId().getProvider());
//        log.info("CustomUserDetailsService loadUserByUsername : " +userEntity.getUserPwd());
//        return new CustomUserDetails(userEntity);
//    }


    // 사용자의 유효성을 검증하는 메서드입니다.
    private UserEntity validateUser(InputUser inputUser){
        UserEntity userEntity = null;
        log.info("CustomUserDetailsService validateUser : " + inputUser.getUserEmail());
        log.info("CustomUserDetailsService validateUser : " + inputUser.getProvider());
        log.info("CustomUserDetailsService validateUser : " + inputUser.getUserPwd());
        // 주어진 이메일로 사용자를 조회합니다. 사용자가 존재하지 않을 경우 UsernameNotFoundException 예외를 발생시킵니다.
        Optional<UserEntity> optionalUser = userRepository.findByEmailAndProvider(inputUser.getUserEmail(), inputUser.getProvider());
        if (optionalUser.isPresent()) {
            userEntity =  optionalUser.get();
        }
        log.info("CustomUserDetailsService validateUser : " + userEntity);
                //.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 이메일입니다: " + inputUser.getUserEmail() + inputUser.getProvider()));

        // 사용자 계정이 삭제된 경우 UsernameNotFoundException 예외를 발생시킵니다.
        if (archivedUserRepository.findByEmailAndProvider(inputUser.getUserEmail(), inputUser.getProvider()).isPresent()) {
            throw new DisabledException ("삭제된 계정입니다: " + inputUser.getUserEmail() + inputUser.getProvider());
        }

        // 사용자 계정이 활성화되지 않은 경우 UsernameNotFoundException 예외를 발생시킵니다.
        if (userEntity.getLoginOk() == 'N') {
            throw new LockedException("활성화되지 않은 계정입니다: " + inputUser.getUserEmail() + inputUser.getProvider());
        }

        log.info("CustomUserDetailsService validateUser return: " + userEntity.getUserId().getUserEmail());
        log.info("CustomUserDetailsService validateUser return: " + userEntity.getUserId().getProvider());
        log.info("CustomUserDetailsService validateUser return: " + userEntity.getUserPwd());
        return userEntity; // 유효한 사용자 정보를 반환합니다.
    }

}
