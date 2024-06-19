package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LectureRepository
    extends JpaRepository<LectureEntity, Integer> {

    List<LectureEntity> findByLecturePackageIdOrderByLectureIdAsc(Long lecturePackageId);
}



