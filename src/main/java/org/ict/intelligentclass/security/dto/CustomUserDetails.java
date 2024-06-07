package org.ict.intelligentclass.security.dto;


import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Spring Security의 UserDetails 인터페이스를 구현한 CustomUserDetails 클래스입니다.
public class CustomUserDetails implements UserDetails {

    private final UserEntity userEntity; // 사용자 정보를 담고 있는 User 엔티티의 인스턴스입니다.

    // 클래스 생성자에서 User 엔티티의 인스턴스를 받아 멤버 변수에 할당합니다.
    public CustomUserDetails(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    // 사용자의 권한 목록을 반환하는 메서드입니다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 사용자의 isAdmin 값에 따라 ROLE_ADMIN 또는 ROLE_USER 권한을 부여합니다.
        if (this.userEntity.getUserType() == 0) {
            authorities.add(new SimpleGrantedAuthority("ROLE_STUDENT"));
        } else if (this.userEntity.getUserType() == 1){
            authorities.add(new SimpleGrantedAuthority("ROLE_TEACHER"));
        } else if (this.userEntity.getUserType() == 2){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }

    // 사용자의 이메일을 반환합니다.
    @Override
    public String getUsername() {
        return userEntity.getUserId().getUserEmail();
    }

    // 제공자를 반환합니다.
    public String getProvider() {
        return userEntity.getUserId().getProvider();
    }

    // 사용자의 비밀번호를 반환합니다.
    @Override
    public String getPassword() {
        return userEntity.getUserPwd();
    }

    // 사용자의 닉네임을 반환합니다.
    public String getNickname() {
        return userEntity.getNickname();
    }

    // 계정이 잠겨있지 않은지를 반환합니다.
    @Override
    public boolean isAccountNonLocked() {
        return (this.userEntity.getLoginOk() == 'Y'); // true => 계정 안잠김, false => 계정 잠김
    }



    // 계정이 만료되었는지를 반환합니다. 여기서는 만료되지 않았다고 가정합니다.
    @Override
    public boolean isAccountNonExpired() {
        return true; // 안쓰는 기능이라 true 반환
    }

    // 사용자의 크리덴셜(비밀번호 등)이 만료되지 않았는지를 반환합니다.
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 안쓰는 기능이라 true 반환
    }

    // 사용자 계정이 활성화(사용 가능) 상태인지를 반환합니다.
    @Override
    public boolean isEnabled() {
        return true; // 안쓰는 기능이라 true 반환
    }
}
