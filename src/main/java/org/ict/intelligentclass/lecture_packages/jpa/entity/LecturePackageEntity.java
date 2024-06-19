package org.ict.intelligentclass.lecture_packages.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecture_package_seq")
    @SequenceGenerator(name = "lecture_package_seq", sequenceName = "LECTURE_PACKAGE_SEQ", allocationSize = 1)
    @Column(name = "LECTURE_PACKAGE_ID")
    private Long lecturePackageId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "PACKAGE_LEVEL", nullable = false)
    private String packageLevel;

    @Column(name = "PRICE_KIND", nullable = false)
    private int priceKind;

    @Column(name = "PRICE", nullable = false)
    private int price;

    @Column(name = "THUMBNAIL", nullable = false)
    private String thumbnail;

    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    private Date registerDate;

    @Column(name = "VIEW_COUNT", nullable = false, columnDefinition = "int default 0")
    private int viewCount;

    @Column(name = "BACKGROUND_COLOR", nullable = false)
    private String backgroundColor;

    @OneToMany(mappedBy = "lecturePackage")
    private Set<PackageSubCategoryEntity> packageSubCategory;

    @OneToMany(mappedBy = "lecturePackage")
    private Set<PackageTechStackEntity> packageTechStack;

//    @OneToMany(mappedBy = "lecturePackage")
//    private Set<RatingEntity> ratings;

    @PrePersist
    protected void onCreate() {
        if (this.registerDate == null) {
            this.registerDate = new Date();
        }
    }

//    public LecturePackageDTO toDTO() {
//        return LecturePackageDTO.builder()
//                .lecturePackageId(this.lecturePackageId)
//                .nickname(this.nickname)
//                .title(this.title)
//                .content(this.content)
//                .packageLevel(this.packageLevel)
//                .priceKind(this.priceKind)
//                .price(this.price)
//                .thumbnail(this.thumbnail)
//                .registerDate(this.registerDate)
//                .viewCount(this.viewCount)
//                .backgroundColor(this.backgroundColor)
//                .build();
//    }
}