package org.ict.intelligentclass.qna.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.qna.jpa.entity.QnaQuestionEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class QnaQuestionDto {

    private int questionId;
    private String questionTitle;
    private String questionContent;
    private String nickname;
    private Date questionDate;
    private String questionCheck;

    public QnaQuestionEntity toEntity() {
        return QnaQuestionEntity.builder()
                .questionId(this.questionId)
                .questionTitle(this.questionTitle)
                .questionContent(this.questionContent)
                .nickname(this.nickname)
                .questionDate(this.questionDate)
                .questionCheck(this.questionCheck)
                .build();
    }

}
