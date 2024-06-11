package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.lecture.jpa.entity.LectureReadEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureReadRepository
    extends JpaRepository<LectureReadEntity, String> {

    // 강의 읽음 처리 가져오기
//    List<LectureReadEntity> findReadById(int lectureId);
}

