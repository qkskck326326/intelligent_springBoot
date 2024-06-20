package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.lecture.jpa.entity.LectureReadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LectureReadRepository extends JpaRepository<LectureReadEntity, Integer> {

    LectureReadEntity findReadByLectureIdAndNickname(int lectureId, String nickname);

    Optional<LectureReadEntity> findByLectureIdAndNickname(int lectureId, String nickname);
}

