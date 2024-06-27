package org.ict.intelligentclass.lecture_packages.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;

import java.time.LocalDateTime;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_LECTURE_PACKAGE_ID")
    @SequenceGenerator(name = "SQ_LECTURE_PACKAGE_ID" , sequenceName = "SQ_LECTURE_PACKAGE_ID", allocationSize = 1)
    @Column(name = "LECTURE_PACKAGE_ID")
    private Long lecturePackageId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "CONTENT", nullable = false)
    private String content;

    @Column(name = "PACKAGE_LEVEL", nullable = false)
    private int packageLevel;

    @Column(name = "PRICE_FOREVER", nullable = false)
    private int priceForever;

    @Column(name = "THUMBNAIL", nullable = false)
    private String thumbnail;

    @Column(name = "REGISTER_DATE", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registerDate;

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
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (registerDate == null) registerDate = now;
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