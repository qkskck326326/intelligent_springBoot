package org.ict.intelligentclass.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.post.jpa.entity.CommentEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private Long postId;
    private String userEmail;
    private String provider;
    private String content;
    private LocalDateTime commentTime;
    private String nickname;
    private String profileImageUrl;


    public CommentEntity toEntity() {
        CommentEntity entity = new CommentEntity();
        entity.setId(this.id);
        entity.setPostId(this.postId);
        entity.setUserEmail(this.userEmail);
        entity.setProvider(this.provider);
        entity.setContent(this.content);
        entity.setCommentTime(this.commentTime);
        return entity;
    }
}
