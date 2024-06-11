package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LectureRepository
    extends JpaRepository<LectureEntity, Integer> {



//    LecturePackageEntity findById(Long lecturePackageId);


//    List<LectureEntity> findByLecturePackageId(Long lecturePackageId);
//


//    List<LecturePreviewDto> getLecturePreviewsByLectureId(int lectureId);
}



