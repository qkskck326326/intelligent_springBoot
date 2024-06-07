package org.ict.intelligentclass.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureCommentEntity;
import org.springframework.stereotype.Component;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class LectureCommentDto {

    private int lectureCommentId;
    private int lectureId;
    private String lectureCommentReply;
    private String lectureCommentContent;
    private Date lectureCommentDate;
    private String nickname;
    private int parentCommentId;

    // 엔티티로 변환하는 메서드
    public LectureCommentEntity toEntity() {
        return LectureCommentEntity.builder()
                .lectureCommentId(this.lectureCommentId)
                .lectureId(this.lectureId)
                .lectureCommentReply(this.lectureCommentReply)
                .lectureCommentContent(this.lectureCommentContent)
                .lectureCommentDate(this.lectureCommentDate)
                .nickname(this.nickname)
                .parentCommentId(this.parentCommentId)
                .build();
    }
}
