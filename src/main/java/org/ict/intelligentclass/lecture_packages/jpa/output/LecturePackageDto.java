package org.ict.intelligentclass.lecture_packages.jpa.output;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturePackageDto {
    private Long lecturePackageId;
    private String nickname;
    private String title;
    private String classGoal;
    private String recommendPerson;
    private int priceKind;
    private int price;
    private String thumbnail;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registerDate;
    private int viewCount;
    private List<PackageSubCategoryId> packageSubCategoryIds;
    private List<PackageTechStackId> packageTechStackIds;

    public LecturePackageDto(LecturePackageEntity lecturePackageEntity) {
        this.lecturePackageId = lecturePackageEntity.getLecturePackageId();
        this.nickname = lecturePackageEntity.getNickname();
        this.title = lecturePackageEntity.getTitle();
        this.classGoal = lecturePackageEntity.getClassGoal();
        this.recommendPerson = lecturePackageEntity.getRecommendPerson();
        this.priceKind = lecturePackageEntity.getPriceKind();
        this.price = lecturePackageEntity.getPrice();
        this.thumbnail = lecturePackageEntity.getThumbnail();
        this.registerDate = lecturePackageEntity.getRegisterDate();
        this.viewCount = lecturePackageEntity.getViewCount();
        this.packageSubCategoryIds = lecturePackageEntity.getPackageSubCategory().stream()
                .map(PackageSubCategoryEntity::getPackageSubCategoryId)
                .collect(Collectors.toList());
        this.packageTechStackIds = lecturePackageEntity.getPackageTechStack().stream()
                .map(PackageTechStackEntity::getPackageTechStackId)
                .collect(Collectors.toList());
    }
}