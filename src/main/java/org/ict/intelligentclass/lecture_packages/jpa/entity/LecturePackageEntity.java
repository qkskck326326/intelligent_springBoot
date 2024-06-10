package org.ict.intelligentclass.lecture_packages.jpa.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDto;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_LECTURE_PACKAGE")
public class LecturePackageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LECTURE_PACKAGE_ID")
    private Long lecturePackageId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CLASS_GOAL", nullable = false)
    private String classGoal;

    @Column(name = "RECOMMEND_PERSON", nullable = false)
    private String recommendPerson;

    @Column(name = "PRICE_KIND", nullable = false)
    private int priceKind;

    @Column(name = "PRICE", nullable = false)
    private int price;

    @Column(name = "THUMBNAIL", nullable = false)
    private String thumbnail;

    @Column(name = "REGISTER_DATE", nullable = false)
    private Date registerDate;

    @Column(name = "VIEW_COUNT", nullable = false, columnDefinition = "int default 0")
    private int viewCount;

    @OneToMany(mappedBy = "lecturePackage")
    private Set<PackageSubCategoryEntity> packageSubCategory;

    @OneToMany(mappedBy = "lecturePackage")
    private Set<PackageTechStackEntity> packageTechStack;


    public LecturePackageDto toDto() {
        return LecturePackageDto.builder()
                .lecturePackageId(this.lecturePackageId)
                .nickname(this.nickname)
                .title(this.title)
                .classGoal(this.classGoal)
                .recommendPerson(this.recommendPerson)
                .priceKind(this.priceKind)
                .price(this.price)
                .thumbnail(this.thumbnail)
                .registerDate(this.registerDate)
                .viewCount(this.viewCount)
                .build();
    }
}