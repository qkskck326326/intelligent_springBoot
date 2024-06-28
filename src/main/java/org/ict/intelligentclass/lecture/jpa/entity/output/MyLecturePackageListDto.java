package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyLecturePackageListDto {

    private Long lecturePackageId;
    private String nickname;
    private String title;
    private String thumbnail;
    private int viewCount;
    private LocalDateTime registerDate;
    private int packageLevel;
    private float rating;

    public MyLecturePackageListDto(LecturePackageEntity lecturePackageEntity, float rating) {
        this.lecturePackageId = lecturePackageEntity.getLecturePackageId();
        this.nickname = lecturePackageEntity.getNickname();
        this.title = lecturePackageEntity.getTitle();
        this.thumbnail = lecturePackageEntity.getThumbnail();
        this.viewCount = lecturePackageEntity.getViewCount();
        this.registerDate = lecturePackageEntity.getRegisterDate();
        this.packageLevel = lecturePackageEntity.getPackageLevel();
        this.rating = rating;
    }
}
