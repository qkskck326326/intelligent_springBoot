package org.ict.intelligentclass.lecture_packages.jpa.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturePackageRegister {
    private Long lecturePackageId;
    private String nickname;
    private String title;
    private String content;
    private String averageClassLength;
    private int packageLevel;
    private int priceForever;
    private String thumbnail;
    private String backgroundColor;
    private List<String> learningContent = new ArrayList<>(); // 학습대상자
    private List<String> readyContent = new ArrayList<>(); // 선수지식
    private List<Long> packageSubCategoryId = new ArrayList<>(); // 빈 리스트로 초기화
    private List<Long> packageTechStackId = new ArrayList<>();


//    public LecturePackageRegister(LecturePackageEntity lecturePackageEntity) {
//        this.lecturePackageId = lecturePackageEntity.getLecturePackageId();
//        this.nickname = lecturePackageEntity.getNickname();
//        this.title = lecturePackageEntity.getTitle();
//        this.content = lecturePackageEntity.getContent();
//        this.packageLevel = lecturePackageEntity.getPackageLevel();
//        this.priceKind = lecturePackageEntity.getPriceKind();
//        this.priceMonth = lecturePackageEntity.getPriceMonth();
//        this.priceForever = lecturePackageEntity.getPriceForever();
//        this.thumbnail = lecturePackageEntity.getThumbnail();
//
//    }
}