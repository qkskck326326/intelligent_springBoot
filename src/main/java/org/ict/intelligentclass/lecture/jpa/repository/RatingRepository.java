package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository
    extends JpaRepository<RatingEntity, String> {

    RatingEntity findByLecturePackageId(Long lecturePackageId);
}



