package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.post.model.dto.CommentDto;
import org.ict.intelligentclass.user.model.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_COMMENT")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_COMMENT_ID")
    @SequenceGenerator(name = "SQ_COMMENT_ID", sequenceName = "SQ_COMMENT_ID", allocationSize = 1)
    @Column(name = "COMMENT_ID")
    private Long id;

    @Column(name = "POST_ID", nullable = false)
    private Long postId;

    @Column(name = "USEREMAIL", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "PROVIDER", nullable = false, length = 20)
    private String provider;

    @Column(name = "CONTENT", nullable = false, length = 4000)
    private String content;

    @Column(name = "COMMENT_TIME", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime commentTime;

    @PrePersist
    protected void onCreate() {
        if (commentTime == null) {
            commentTime = LocalDateTime.now();
        }
    }

    public CommentDto toDto(UserDto userDto) {
        CommentDto dto = new CommentDto();
        dto.setId(this.id);
        dto.setPostId(this.postId);
        dto.setUserEmail(this.userEmail);
        dto.setProvider(this.provider);
        dto.setContent(this.content);
        dto.setCommentTime(this.commentTime);
        dto.setNickname(userDto.getNickname());
        dto.setProfileImageUrl(userDto.getProfileImageUrl());
        return dto;
    }

}


