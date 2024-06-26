package org.ict.intelligentclass.qna.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.qna.model.dto.QnaQuestionDto;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_QNA_QUESTION")
public class QnaQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qna_question_seq_generator")
    @SequenceGenerator(name = "qna_question_seq_generator", sequenceName = "SQ_QNA_QUESTION_ID", allocationSize = 1)
    @Column(name = "QUESTION_ID")
    private int questionId;

    @Column(name = "QUESTION_TITLE", nullable = false)
    private String questionTitle;

    @Column(name = "QUESTION_CONTENT", nullable = false)
    private String questionContent;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "QUESTION_DATE", nullable = false)
    private Date questionDate;

    @Column(name = "QUESTION_CHECK", columnDefinition = "CHAR(1)")
    private String questionCheck;

    public QnaQuestionDto toDto() {
        return QnaQuestionDto.builder()
                .questionId(questionId)
                .questionTitle(questionTitle)
                .questionContent(questionContent)
                .nickname(nickname)
                .questionDate(questionDate)
                .questionCheck(questionCheck)
                .build();
    }
}
