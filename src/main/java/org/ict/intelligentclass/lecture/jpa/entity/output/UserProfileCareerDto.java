package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.career.jpa.entity.CareerEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCareerDto {

    private String nickname;
    private String institutionName;
    private String department;
    private String position;
    private Date startDate;
    private Date endDate;
    private String responsibilities;

    public UserProfileCareerDto(CareerEntity careerEntity) {
        this.nickname = careerEntity.getNickname();
        this.institutionName = careerEntity.getInstitutionName();
        this.department = careerEntity.getDepartment();
        this.position = careerEntity.getPosition();
        this.startDate = careerEntity.getStartDate();
        this.endDate = careerEntity.getEndDate();
        this.responsibilities = careerEntity.getResponsibilities();
    }
}
