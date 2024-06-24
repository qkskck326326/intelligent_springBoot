package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureCommentEntity;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private int lectureId;
    private int lectureCommentId;
    private String nickname;
    private String lectureCommentContent;
    private Date lectureCommentDate;
    private Integer parentCommentId;
    private String lectureCommentReply;
    private String profileImageUrl;

    public CommentDto(LectureCommentEntity lectureCommentEntity, UserEntity userEntity) {
        this.lectureId = lectureCommentEntity.getLectureId();
        this.lectureCommentId = lectureCommentEntity.getLectureCommentId();
        this.nickname = lectureCommentEntity.getNickname();
        this.lectureCommentContent = lectureCommentEntity.getLectureCommentContent();
        this.lectureCommentDate = lectureCommentEntity.getLectureCommentDate();
        this.parentCommentId = lectureCommentEntity.getParentCommentId();
        this.lectureCommentReply = lectureCommentEntity.getLectureCommentReply();
        this.profileImageUrl = userEntity.getProfileImageUrl();
    }
}
