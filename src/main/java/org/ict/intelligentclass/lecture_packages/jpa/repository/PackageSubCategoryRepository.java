package org.ict.intelligentclass.lecture_packages.jpa.repository;


import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageSubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface PackageSubCategoryRepository extends JpaRepository<PackageSubCategoryEntity, Long> {

    @Query("SELECT ps FROM PackageSubCategoryEntity ps WHERE ps.packageSubCategoryId.lecturePackageId = :lecturePackageId")
    List<PackageSubCategoryEntity> findByLecturePackageId(@Param("lecturePackageId") Long lecturePackageId);

    void deleteAllByPackageSubCategoryId_LecturePackageId(Long lecturePackageId);

    @Query("SELECT ps FROM PackageSubCategoryEntity ps WHERE ps.subCategory.id = :categoryId")
    List<PackageSubCategoryEntity> categorySortPackages(@Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT ps FROM PackageSubCategoryEntity ps WHERE ps.subCategory.id IN :subCategoryIds")
    List<PackageSubCategoryEntity> findBySubCategoryIdIn(@Param("subCategoryIds") List<Long> subCategoryIds);

    @Query("SELECT ps FROM PackageSubCategoryEntity ps WHERE ps.subCategory.upperCategory.id IN :upperCategoryIds")
    List<PackageSubCategoryEntity> findBySubCategoryUpperCategoryId(@Param("upperCategoryIds") Long upperCategoryIds);

}
