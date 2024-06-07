package org.ict.intelligentclass.lecture_packages.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_PACKAGE_SUB_CATEGORY")
public class PackageSubCategoryEntity {

    @EmbeddedId
    private PackageSubCategoryId id;

    @ManyToOne
    @MapsId("lecturePackageId")
    @JoinColumn(name = "LECTURE_PACKAGE_ID", insertable = false, updatable = false)
    private LecturePackageEntity lecturePackage;

    @ManyToOne
    @MapsId("subCategoryId")
    @JoinColumn(name = "SUB_CATEGORY_ID", insertable = false, updatable = false)
    private SubCategoryEntity subCategory;
}