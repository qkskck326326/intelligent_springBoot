package org.ict.intelligentclass.qna.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.qna.model.dto.QnaAnswerDto;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_QNA_ANSWER")
public class QnaAnswerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "qna_answer_seq_generator")
    @SequenceGenerator(name = "qna_answer_seq_generator", sequenceName = "SQ_QNA_ANSWER_ID", allocationSize = 1)
    @Column(name = "ANSWER_ID")
    private int answerId;

    @Column(name = "QUESTION_ID", nullable = false)
    private int questionId;

    @Column(name = "ANSWER_CONTENT", nullable = false)
    private String answerContent;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ANSWER_DATE", nullable = false)
    private Date answerDate;

    public QnaAnswerDto toDto() {
        return QnaAnswerDto.builder()
                .answerId(answerId)
                .questionId(questionId)
                .answerContent(answerContent)
                .nickname(nickname)
                .answerDate(answerDate)
                .build();
    }



}
