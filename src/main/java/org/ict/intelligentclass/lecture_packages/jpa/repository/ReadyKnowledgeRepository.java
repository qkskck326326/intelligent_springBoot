package org.ict.intelligentclass.lecture_packages.jpa.repository;


import jakarta.transaction.Transactional;
import org.ict.intelligentclass.lecture_packages.jpa.entity.ReadyKnowledgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadyKnowledgeRepository extends JpaRepository<ReadyKnowledgeEntity, Long> {

    @Query("SELECT r FROM ReadyKnowledgeEntity r WHERE r.lecturePackage.lecturePackageId = :lecturePackageId")
    List<ReadyKnowledgeEntity> findByReadyContent(@Param("lecturePackageId") Long lecturePackageId);


    @Query("SELECT r FROM ReadyKnowledgeEntity r WHERE r.lecturePackage.lecturePackageId = :lecturePackageId")
    List<ReadyKnowledgeEntity> findByLecturePackageId(@Param("lecturePackageId") Long lecturePackageId);


    @Modifying
    @Transactional
    @Query("DELETE FROM ReadyKnowledgeEntity r WHERE r.lecturePackage.lecturePackageId = :lecturePackageId")
    void deleteByReadyContent(@Param("lecturePackageId") Long lecturePackageId);

}
