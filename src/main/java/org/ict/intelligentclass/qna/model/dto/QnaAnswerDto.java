package org.ict.intelligentclass.qna.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.qna.jpa.entity.QnaAnswerEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class QnaAnswerDto {

    private int answerId;
    private int questionId;
    private String answerContent;
    private String nickname;
    private Date answerDate;

    public QnaAnswerEntity toEntity() {
        return QnaAnswerEntity.builder()
               .answerId(this.answerId)
               .questionId(this.questionId)
               .answerContent(this.answerContent)
               .nickname(this.nickname)
               .answerDate(this.answerDate)
               .build();
    }

}
