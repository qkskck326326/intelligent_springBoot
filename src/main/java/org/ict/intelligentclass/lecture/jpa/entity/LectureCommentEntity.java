package org.ict.intelligentclass.lecture.jpa.entity;

import jakarta.persistence.*;
import lombok.*;
import org.ict.intelligentclass.lecture.model.dto.LectureCommentDto;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_LECTURE_COMMENT")
public class LectureCommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LECTURE_COMMENT_ID")
    private int lectureCommentId;

    @Column(name = "LECTURE_ID", nullable = false)
    private int lectureId;

    @Column(name = "LECTURE_COMMENT_REPLY", columnDefinition = "CHAR(1)")
    private String lectureCommentReply;

    @Column(name = "LECTURE_COMMENT_CONTENT", length = 255)
    private String lectureCommentContent;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LECTURE_COMMENT_DATE", nullable = false)
    private Date lectureCommentDate;

    @Column(name = "NICKNAME", nullable = false, length = 50)
    private String nickname;

    @Column(name = "PARENT_COMMENT_ID")
    private Integer parentCommentId;

    // entity -> dto 변환 메서드 추가
    public LectureCommentDto toDto() {
        return LectureCommentDto.builder()
                .lectureCommentId(lectureCommentId)
                .lectureId(lectureId)
                .lectureCommentReply(lectureCommentReply)
                .lectureCommentContent(lectureCommentContent)
                .lectureCommentDate(lectureCommentDate)
                .nickname(nickname)
                .parentCommentId(parentCommentId)
                .build();
    }
}
