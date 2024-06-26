package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.education.jpa.entity.EducationEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileEduDto {

    private String nickname;
    private String educationLevel;
    private String schoolName;
    private String major;
    private String educationStatus;
    private Date graduationDate;
    private Date passDate;
    private String homeAndTransfer;
//    private String universityLevel;

    public UserProfileEduDto(EducationEntity educationEntity) {
        this.nickname = educationEntity.getNickname();
        this.educationLevel = educationEntity.getEducationLevel();
        this.schoolName = educationEntity.getSchoolName();
        this.major = educationEntity.getMajor();
        this.educationStatus = educationEntity.getEducationStatus();
        this.graduationDate = educationEntity.getGraduationDate();
        this.passDate = educationEntity.getPassDate();
        this.homeAndTransfer = educationEntity.getHomeAndTransfer();
//        this.universityLevel = educationEntity.getUniversityLevel();
    }
}
