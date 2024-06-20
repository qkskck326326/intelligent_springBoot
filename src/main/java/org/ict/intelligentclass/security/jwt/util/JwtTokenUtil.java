package org.ict.intelligentclass.security.jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Component
public class JwtTokenUtil {

    // JWT 생성과 검증에 사용될 비밀키와 만료 시간을 필드로 선언합니다.
    private final SecretKey secretKey;
    private final UserRepository userRepository;

    // 생성자를 통해 application.properties에서 정의된 JWT 비밀키와 만료 시간, UserRepository를 주입받습니다.
    public JwtTokenUtil(@Value("${jwt.secret}") String secret, UserRepository userRepository) {
        // 비밀키를 초기화합니다. 이 비밀키는 JWT의 서명에 사용됩니다.
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        this.userRepository = userRepository; // 사용자 정보를 조회하기 위한 UserRepository 인스턴스를 초기화합니다.
    }

    // 사용자의 이메일을 기반으로 JWT를 생성합니다.
    public String generateToken(String userEmail, String provider, String tokenType, Long expiredMs) {
        // UserRepository를 사용해 사용자 정보를 조회합니다.
        Optional<UserEntity> userEntity = userRepository.findByEmailAndProvider(userEmail, provider);

        // 사용자 정보가 없는 경우, UsernameNotFoundException을 발생시킵니다.
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User with email " + userEmail + " not found");
        }

        // 사용자의 유저타입을 확인합니다.
        int userType = userEntity.get().getUserType();
        String nickname = userEntity.get().getNickname();

//        log.info(new Date(System.currentTimeMillis() + expiredMs));

        // JWT를 생성합니다. 여기서는 사용자 이메일을 주체(subject)로, 관리자 여부를 클레임으로 추가합니다.
        return Jwts.builder()
                .setSubject(userEmail)
                .claim("provider", provider) // "provider" 클레임에 provider 정보를 설정합니다.
                .claim("userType", userType)
                .claim("tokenType",tokenType)
                .claim("nickname",nickname)
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 시간 설정
                .signWith(secretKey, SignatureAlgorithm.HS256) // 비밀키와 HS256 알고리즘으로 JWT를 서명
                .compact(); // JWT 문자열을 생성
    }

    // JWT에서 사용자 이메일을 추출합니다.
    public String getUserEmailFromToken(String tokenValue) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(tokenValue).getBody();
        return claims.getSubject();
    }

    // JWT에서 사용자 제공자을 추출합니다.
    public String getProviderFromToken(String tokenValue) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(tokenValue).getBody();
        return claims.get("provider", String.class);
    }


    // JWT에서 사용자 이메일, 제공자 추출합니다.
    public UserId getUserIdFromToken(String tokenValue) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(tokenValue).getBody();
        return new UserId(claims.getSubject(), claims.get("provider", String.class));
    }

    // JWT의 만료 여부를 검증합니다.
    public boolean isTokenExpired(String tokenValue) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setAllowedClockSkewSeconds(10) //  setAllowedClockSkewSeconds(10)를 추가하여 5초의 클럭 스큐를 허용했습니다. 이로 인해 JWT 파싱 시 최대 10초의 시간 차이를 무시할 수 있습니다.
                .build()
                .parseClaimsJws(tokenValue)
                .getBody();
        return claims.getExpiration().before(new Date());
    }

    // JWT에서 사용자의 관리자 여부를 확인합니다.
    public int getUserTypeFromToken(String tokenValue) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(tokenValue).getBody();
        return claims.get("userType", Integer.class);
    }


    // JWT에서 토큰의 카테고리를 확인합니다.
    public String getTokenTypeFromToken(String tokenValue) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(tokenValue).getBody();
        return claims.get("tokenType", String.class);
    }

    public Claims getClaimsFromToken(String tokenValue) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(tokenValue).getBody();
    }

    public boolean validateToken(String tokenValue) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(tokenValue);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}