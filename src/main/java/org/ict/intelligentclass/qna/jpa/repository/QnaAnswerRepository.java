package org.ict.intelligentclass.qna.jpa.repository;

import org.ict.intelligentclass.qna.jpa.entity.QnaAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaAnswerRepository extends JpaRepository<QnaAnswerEntity, String> {

    List<QnaAnswerEntity> findByQuestionId(int questionId);
}
