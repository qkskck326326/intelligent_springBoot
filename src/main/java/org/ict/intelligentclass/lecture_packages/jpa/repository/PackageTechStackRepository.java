package org.ict.intelligentclass.lecture_packages.jpa.repository;



import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageTechStackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PackageTechStackRepository extends JpaRepository<PackageTechStackEntity, Long> {

    @Query("SELECT pt FROM PackageTechStackEntity pt WHERE pt.packageTechStackId.lecturePackageId = :lecturePackageId")
    List<PackageTechStackEntity> findByLecturePackageId(@Param("lecturePackageId") Long lecturePackageId);
}