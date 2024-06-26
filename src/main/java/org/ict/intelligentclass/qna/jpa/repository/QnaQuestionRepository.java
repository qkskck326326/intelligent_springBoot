package org.ict.intelligentclass.qna.jpa.repository;

import org.ict.intelligentclass.qna.jpa.entity.QnaQuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QnaQuestionRepository extends JpaRepository<QnaQuestionEntity, String> {

    Page<QnaQuestionEntity> findByNicknameAndQuestionCheck(String nickname, String questionCheck, Pageable pageable);

    Optional<QnaQuestionEntity> findByQuestionId(int questionId);

    Page<QnaQuestionEntity> findByQuestionCheckOrderByQuestionDateAsc(String checkStatus, Pageable pageable);
}
