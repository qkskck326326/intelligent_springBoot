package org.ict.intelligentclass.lecture_packages.jpa.repository;


import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturePackageRepository extends JpaRepository<LecturePackageEntity, Long> {

    @Query("SELECT l FROM LecturePackageEntity l ORDER BY l.registerDate desc")
    List<LecturePackageEntity> findAllSort();


    @Query("SELECT l FROM LecturePackageEntity l WHERE l.lecturePackageId = :packageId")
    List<LecturePackageEntity> findByCategorySortPackages(@Param("packageId") Long packageId);

    List<LecturePackageEntity> findByLecturePackageIdIn(List<Long> lecturePackageIds);









}