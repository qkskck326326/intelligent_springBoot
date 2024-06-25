package org.ict.intelligentclass.qna.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.qna.jpa.entity.QnaAnswerEntity;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnswerDto {

    private int answerId;
    private int questionId;
    private String answerContent;
    private String nickname;
    private Date answerDate;

    public AnswerDto(QnaAnswerEntity qnaAnswerEntity, UserEntity userEntity) {
        this.answerId = qnaAnswerEntity.getAnswerId();
        this.questionId = qnaAnswerEntity.getQuestionId();
        this.answerContent = qnaAnswerEntity.getAnswerContent();
        this.nickname = userEntity.getNickname();
        this.answerDate = qnaAnswerEntity.getAnswerDate();
    }
}
