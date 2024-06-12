package org.ict.intelligentclass.lecture_packages.jpa.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturePackageRegister {
    private Long lecturePackageId;
    private String nickname;
    private String title;
    private String classGoal;
    private String recommendPerson;
    private int priceKind;
    private int price;
    private String thumbnail;
    private List<Long> packageSubCategoryId;
    private List<Long> packageTechStackId;


    public LecturePackageRegister(LecturePackageEntity lecturePackageEntity) {
        this.lecturePackageId = lecturePackageEntity.getLecturePackageId();
        this.nickname = lecturePackageEntity.getNickname();
        this.title = lecturePackageEntity.getTitle();
        this.classGoal = lecturePackageEntity.getClassGoal();
        this.recommendPerson = lecturePackageEntity.getRecommendPerson();
        this.priceKind = lecturePackageEntity.getPriceKind();
        this.price = lecturePackageEntity.getPrice();
        this.thumbnail = lecturePackageEntity.getThumbnail();

    }
}