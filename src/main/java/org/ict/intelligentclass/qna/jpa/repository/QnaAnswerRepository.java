package org.ict.intelligentclass.qna.jpa.repository;

import org.ict.intelligentclass.qna.jpa.entity.QnaAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface QnaAnswerRepository extends JpaRepository<QnaAnswerEntity, String> {

    Optional<QnaAnswerEntity> findByQuestionId(int questionId);
}
