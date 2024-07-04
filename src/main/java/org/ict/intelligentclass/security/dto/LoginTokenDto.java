package org.ict.intelligentclass.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.security.entity.LoginTokenEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginTokenDto {
    private String userEmail;
    private String provider;
//    private String accessTokenValue;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date accessTokenCreatedTime;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date accessTokenExpirationTime;
    private String refreshTokenValue;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refreshTokenCreatedTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime refreshTokenExpirationTime;

    public LoginTokenEntity toEntity() {
        UserId userId = new UserId(this.userEmail, this.provider);
        return LoginTokenEntity.builder()
                .userId(userId)
//                .accessTokenValue(this.accessTokenValue)
//                .accessTokenCreatedTime(this.accessTokenCreatedTime)
//                .accessTokenExpirationTime(this.accessTokenExpirationTime)
                .refreshTokenValue(this.refreshTokenValue)
                .refreshTokenCreatedTime(this.refreshTokenCreatedTime)
                .refreshTokenExpirationTime(this.refreshTokenExpirationTime)
                .build();
    }
}