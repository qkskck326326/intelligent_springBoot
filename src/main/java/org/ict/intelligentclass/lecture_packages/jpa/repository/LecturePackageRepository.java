package org.ict.intelligentclass.lecture_packages.jpa.repository;


import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturePackageRepository extends JpaRepository<LecturePackageEntity, Long> {

    @Query("SELECT l FROM LecturePackageEntity l ORDER BY l.registerDate desc")
    List<LecturePackageEntity> findAllSort();





}