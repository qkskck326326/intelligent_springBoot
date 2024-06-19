package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;

// 실험용
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureOwnerDto {
    private Long lecturePackageId;
    private String nickname;

    public LectureOwnerDto(LecturePackageEntity lecturePackageEntity) {
        this.lecturePackageId = lecturePackageEntity.getLecturePackageId();
        this.nickname = lecturePackageEntity.getNickname();
    }
}
