package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.lecture.jpa.entity.LectureCommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureCommentRepository
    extends JpaRepository<LectureCommentEntity, String> {

    List<LectureCommentEntity> findByLectureIdOrderByLectureCommentDateDesc(int lectureId);

    Optional<LectureCommentEntity> findByLectureCommentId(int lectureCommentId);

    void deleteByLectureCommentId(int lectureCommentId);
}



