package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LectureRepository
    extends JpaRepository<LectureEntity, Integer> {

    // List<LectureEntity> findAll();

    List<LectureEntity> findByLecturePackageId(Long lecturePackageId);

    @Query("select l from LectureEntity l join l.lectureReads r")
    List<LectureEntity> findListWithRead();

//    List<LecturePreviewDto> getLecturePreviewsByLectureId(int lectureId);
}



