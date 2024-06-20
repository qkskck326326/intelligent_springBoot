package org.ict.intelligentclass.user.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.report.jpa.entity.ReportEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.model.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_USERS")
public class UserEntity {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "userId.userEmail", column = @Column(name = "USEREMAIL")),
            @AttributeOverride(name = "userId.provider", column = @Column(name = "PROVIDER"))
    })
    private UserId userId;

    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    @Column(name = "USER_PWD")
    private String userPwd;

    @Column(name = "PHONE", unique = true)
    private String phone;

    @Column(name = "NICKNAME", unique = true)
    private String nickname;

    @Column(name = "REGISTER_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registerTime;

    @Column(name = "PROFILE_IMAGE_URL", nullable = false)
    private String profileImageUrl;

    @Column(name = "USER_TYPE", nullable = false)
    private int userType;

    @Column(name = "REPORT_COUNT", nullable = false)
    private int reportCount;

    @Column(name = "LOGIN_OK", nullable = false)
    private char loginOk;

    @Column(name = "FACE_LOGIN_YN", nullable = false)
    private char faceLoginYn;

    @Column(name = "sns_access_token", nullable = false)
    private String snsAccessToken;

    @OneToMany(mappedBy = "receiveUser")
    private List<ReportEntity> receivedReports;

    @OneToMany(mappedBy = "doUser")
    private List<ReportEntity> reportsMade;





    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (registerTime == null) registerTime = now;
    }

    //entity -> dto method
    public UserDto toDto() {
        return UserDto.builder()
                .userEmail(this.userId.getUserEmail())
                .provider(this.userId.getProvider())
                .userName(this.userName)
                .userPwd(this.userPwd)
                .phone(this.phone)
                .nickname(this.nickname)
                .registerTime(this.registerTime)
                .profileImageUrl(this.profileImageUrl)
                .userType(this.userType)
                .reportCount(this.reportCount)
                .loginOk(this.loginOk)
                .faceLoginYn(this.faceLoginYn)
                .snsAccessToken(this.snsAccessToken)
                .build();
    }
}
