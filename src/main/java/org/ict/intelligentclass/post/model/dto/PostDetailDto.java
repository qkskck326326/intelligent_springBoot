package org.ict.intelligentclass.post.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.post.jpa.entity.CommentEntity;
import org.ict.intelligentclass.post.jpa.entity.FileEntity;
import org.ict.intelligentclass.post.jpa.entity.PostEntity;
import org.ict.intelligentclass.user.model.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDetailDto {
    private Long Id;
    private String userEmail;
    private String provider;
    private Long subCategoryId;
    private String title;
    private String content;
    private LocalDateTime postTime;
    private int viewCount;
    private String nickname;
    private String profileImageUrl;
    private String categoryName;
    private long commentCount;
    private long likeCount;
    private boolean userLiked;
    private boolean userBookmarked; // 북마크 상태 추가
    private List<CommentDto> comments;
    private List<FileEntity> files;
    private List<String> tags;

    // Getters and setters

    public PostEntity toEntity() {
        PostEntity postEntity = new PostEntity();
        postEntity.setId(Id);
        postEntity.setUserEmail(userEmail);
        postEntity.setProvider(provider);
        postEntity.setSubCategoryId(subCategoryId);
        postEntity.setTitle(title);
        postEntity.setContent(content);
        postEntity.setPostTime(postTime);
        postEntity.setViewCount(viewCount);
        return postEntity;
    }



}
