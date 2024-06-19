package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface RatingRepository
    extends JpaRepository<RatingEntity, String> {

    @Query("SELECT AVG(r.rating) FROM RatingEntity r WHERE r.lecturePackageId = :lecturePackageId")
    Double findAverageRatingByLecturePackageId(Long lecturePackageId);

    RatingEntity findByLecturePackageId(Long lecturePackageId);

    boolean existsByLecturePackageIdAndNickname(Long lecturePackageId, String nickname);

    @Query("SELECT r FROM RatingEntity r WHERE r.lecturePackageId IN :lecturePackageIds")
    List<RatingEntity> findByLecturePackageIdIn(@Param("lecturePackageIds") List<Long> lecturePackageIds);


}



