package org.ict.intelligentclass.user.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.career.jpa.entity.CareerEntity;
import org.ict.intelligentclass.education.jpa.entity.EducationEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollForm {
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

    private List<Long> interests;

    private List<EducationEntity> educations;
    private List<CareerEntity> careers;
}
