package org.ict.intelligentclass.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.security.dto.LoginTokenDto;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_LOGIN_TOKEN")
public class LoginTokenEntity {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "userId.userEmail", column = @Column(name = "USEREMAIL")),
            @AttributeOverride(name = "userId.provider", column = @Column(name = "PROVIDER"))
    })
    private UserId userId;

//    @Column(name = "ACCESS_TOKEN_VALUE", unique = true)
//    private String accessTokenValue;
//
//    @Column(name = "ACCESS_TOKEN_CREATED_TIME")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date accessTokenCreatedTime;
//
//    @Column(name = "ACCESS_TOKEN_EXPIRATION_TIME")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date accessTokenExpirationTime;

    @Column(name = "REFRESH_TOKEN_VALUE", unique = true)
    private String refreshTokenValue;

    @Column(name = "REFRESH_TOKEN_CREATED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime refreshTokenCreatedTime;

    @Column(name = "REFRESH_TOKEN_EXPIRATION_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime refreshTokenExpirationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumns({
            @JoinColumn(name = "USEREMAIL", referencedColumnName = "userEmail"),
            @JoinColumn(name = "PROVIDER", referencedColumnName = "provider")
    })
    private UserEntity user;

    public LoginTokenDto toDto() {
        return LoginTokenDto.builder()
                .userEmail(this.userId.getUserEmail())
                .provider(this.userId.getProvider())
//                .accessTokenValue(accessTokenValue)
//                .accessTokenCreatedTime(accessTokenCreatedTime)
//                .accessTokenExpirationTime(accessTokenExpirationTime)
                .refreshTokenValue(refreshTokenValue)
                .refreshTokenCreatedTime(refreshTokenCreatedTime)
                .refreshTokenExpirationTime(refreshTokenExpirationTime)
                .build();
    }
}