package org.ict.intelligentclass.lecture_packages.jpa.repository;

import jakarta.transaction.Transactional;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LearningPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPersonRepository extends JpaRepository<LearningPersonEntity, Long> {

    @Query("SELECT l FROM LearningPersonEntity l WHERE l.lecturePackage.lecturePackageId = :lecturePackageId")
    List<LearningPersonEntity> findByLearningContent(@Param("lecturePackageId") Long lecturePackageId);



    @Query("SELECT l FROM LearningPersonEntity l WHERE l.lecturePackage.lecturePackageId = :lecturePackageId")
    List<LearningPersonEntity> findByLecturePackageId(@Param("lecturePackageId") Long lecturePackageId);



    @Modifying
    @Transactional
    @Query("DELETE FROM LearningPersonEntity l WHERE l.lecturePackage.lecturePackageId = :lecturePackageId")
    void deleteByLearningContent(@Param("lecturePackageId") Long lecturePackageId);
}
