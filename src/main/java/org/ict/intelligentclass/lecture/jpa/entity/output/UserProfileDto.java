package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {

    private String profileImageUrl;
    private String nickname;
    private LocalDateTime registerTime;
    private String userType;
    private List<UserProfileEduDto> educationList;
    private List<UserProfileCareerDto> careerList;
    private List<UserProfileCertifiDto> certificateList;

    public UserProfileDto(UserEntity userEntity, List<UserProfileEduDto> educationList, List<UserProfileCareerDto> careerList, List<UserProfileCertifiDto> certificateList) {
        this.profileImageUrl = userEntity.getProfileImageUrl();
        this.nickname = userEntity.getNickname();
        this.registerTime = userEntity.getRegisterTime();
        this.userType = mapUserType(userEntity.getUserType());
        this.educationList = educationList;
        this.careerList = careerList;
        this.certificateList = certificateList;
    }

    private String mapUserType(int userType) {
        switch (userType) {
            case 0:
                return "학생";
            case 1:
                return "강사";
            case 2:
                return "관리자";
            default:
                return "알 수 없음";
        }
    }
}
