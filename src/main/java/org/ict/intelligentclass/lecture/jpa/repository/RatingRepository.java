package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository
    extends JpaRepository<RatingEntity, String> {

    @Query("SELECT AVG(r.rating) FROM RatingEntity r WHERE r.lecturePackageId = :lecturePackageId")
    Double findAverageRatingByLecturePackageId(Long lecturePackageId);

    RatingEntity findByLecturePackageId(Long lecturePackageId);
}



