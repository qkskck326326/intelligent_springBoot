package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.post.model.dto.CommentDto;
import org.ict.intelligentclass.post.model.dto.PostDetailDto;
import org.ict.intelligentclass.post.model.dto.PostDto;
import org.ict.intelligentclass.user.model.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="TB_POST")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_POST_ID")
    @SequenceGenerator(name = "SQ_POST_ID", sequenceName = "SQ_POST_ID", allocationSize = 1)
    @Column(name = "POST_ID")
    private Long id;

    @Column(name = "USEREMAIL", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "PROVIDER", nullable = false, length = 20)
    private String provider;

    @Column(name = "SUB_CATEGORY_ID", nullable = false)
    private Long subCategoryId;

    @Column(name = "TITLE", nullable = false, length = 255)
    private String title;

    @Column(name = "CONTENT", nullable = false, length = 4000)
    private String content;

    @Column(name = "POST_TIME", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime postTime;

    @Column(name = "POST_VIEW_COUNT", columnDefinition = "NUMBER DEFAULT 0")
    private int viewCount;

    @PrePersist
    protected void onCreate() {
        if (postTime == null) {
            postTime = LocalDateTime.now();
        }
    }

    public PostDto toDto(UserDto userDto, String categoryName, int likeCount, int commentCount, List<String> tags) {
        PostDto dto = new PostDto();
        dto.setId(this.id);
        dto.setTitle(this.title);
        dto.setContentSnippet(this.content.substring(0, Math.min(this.content.length(), 150)));
        dto.setNickname(userDto.getNickname());
        dto.setProfileImageUrl(userDto.getProfileImageUrl());
        dto.setCategoryName(categoryName);
        dto.setLikeCount(likeCount);
        dto.setCommentCount(commentCount);
        dto.setViewCount(this.viewCount);
        dto.setPostTime(this.postTime);
        dto.setTags(tags); // 태그 추가
        return dto;
    }

    public PostDetailDto toDetailDto(UserDto userDto, String categoryName, boolean userLiked, long likeCount, long commentCount, List<CommentDto> commentDtos, List<FileEntity> files, List<String> tags) {
        PostDetailDto dto = new PostDetailDto();
        dto.setId(this.id);
        dto.setUserEmail(this.userEmail);
        dto.setProvider(this.provider);
        dto.setSubCategoryId(this.subCategoryId);
        dto.setTitle(this.title);
        dto.setContent(this.content);
        dto.setPostTime(this.postTime);
        dto.setViewCount(this.viewCount);
        dto.setNickname(userDto.getNickname());
        dto.setProfileImageUrl(userDto.getProfileImageUrl());
        dto.setCategoryName(categoryName);
        dto.setUserLiked(userLiked);
        dto.setLikeCount(likeCount);
        dto.setCommentCount(commentCount);
        dto.setComments(commentDtos);
        dto.setFiles(files);
        dto.setTags(tags); // 태그 추가
        return dto;
    }

}
