package org.ict.intelligentclass.qna.jpa.entity.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuestionInput {

    private String questionTitle;
    private String questionContent;
    private String nickname;
}
