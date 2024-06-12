package org.ict.intelligentclass.post.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

//    public PostEntity toEntity(User user, SubCategory subCategory) {
//        PostEntity postEntity = new PostEntity();
//        postEntity.setTitle(this.title);
//        postEntity.setContent(this.content);
//        postEntity.setPostTime(this.postTime);
//        postEntity.setViewCount(this.postViewCount != null ? this.postViewCount.intValue() : 0);
//        postEntity.setUser(user);
//        postEntity.setSubCategory(subCategory);
//        return postEntity;
//    }

}
