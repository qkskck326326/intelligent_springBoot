package org.ict.intelligentclass.qna.jpa.entity.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnswerInput {

    private int questionId;
    private String answerContent;
    private String nickname;
}
