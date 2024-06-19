package org.ict.intelligentclass.post.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.post.jpa.entity.PostEntity;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String userEmail;
    private String provider;
    private Long subCategoryId;
    private String title;
    private String content;
    private LocalDateTime postTime;
    private int viewCount;
    private String contentSnippet;
    private String nickname;
    private String categoryName;
    private long likeCount;
    private long commentCount;
    private String profileImageUrl; // 프로필 사진 URL 필드 추가

    public PostEntity toEntity() {
        PostEntity entity = new PostEntity();
        entity.setId(this.id);
        entity.setTitle(this.title);
        entity.setContent(this.contentSnippet);
        entity.setUserEmail(this.nickname); // Assume nickname is userEmail in this context
        entity.setProvider(this.provider); // Add provider field in PostDto
        entity.setSubCategoryId(this.subCategoryId); // Add subCategoryId field in PostDto
        entity.setViewCount(this.viewCount);
        return entity;
    }


}
