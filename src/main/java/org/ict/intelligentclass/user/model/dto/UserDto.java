package org.ict.intelligentclass.user.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String userEmail;
    private String provider;
    private String userName;
    private String userPwd;
    private String phone;
    private String nickname;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;
    private String profileImageUrl;
    private int userType;
    private int reportCount;
    private char loginOk;
    private char faceLoginYn;
    private String snsAccessToken;
    private char teacherApply;

    // dto -> entity method
    public UserEntity toEntity() {
        UserId userId = new UserId(this.userEmail, this.provider);
        return UserEntity.builder()
                .userId(userId)
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
                .teacherApply(this.teacherApply)
                .build();
    }
}