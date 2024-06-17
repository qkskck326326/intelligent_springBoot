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

}
