package org.ict.intelligentclass.qna.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.qna.jpa.entity.QnaQuestionEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDto {

    private int questionId;
    private String questionTitle;
    private String questionContent;
    private String nickname;
    private Date questionDate;
    private String questionCheck;

    public QuestionDto(QnaQuestionEntity qnaQuestionEntity) {
        this.questionId = qnaQuestionEntity.getQuestionId();
        this.questionTitle = qnaQuestionEntity.getQuestionTitle();
        this.questionContent = qnaQuestionEntity.getQuestionContent();
        this.nickname = qnaQuestionEntity.getNickname();
        this.questionDate = qnaQuestionEntity.getQuestionDate();
        this.questionCheck = qnaQuestionEntity.getQuestionCheck();
    }
}
