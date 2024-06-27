package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureCommentEntity;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyLecturePackageListDto {

    private Long lecturePackageId;
    private String nickname;
    private String title;
    private String thumbnail;
    private int viewCount;
    private Date registerDate;
    private int packageLevel;
    private int ratingId;
    private float rating;

    public MyLecturePackageListDto(LecturePackageEntity lecturePackageEntity, RatingEntity ratingEntity) {
        this.lecturePackageId = lecturePackageEntity.getLecturePackageId();
        this.nickname = lecturePackageEntity.getNickname();
        this.title = lecturePackageEntity.getTitle();
        this.thumbnail = lecturePackageEntity.getThumbnail();
        this.viewCount = lecturePackageEntity.getViewCount();
        this.registerDate = lecturePackageEntity.getRegisterDate();
        this.packageLevel = lecturePackageEntity.getPackageLevel();
        this.ratingId = ratingEntity.getRatingId();
        this.rating = ratingEntity.getRating();
    }
}
