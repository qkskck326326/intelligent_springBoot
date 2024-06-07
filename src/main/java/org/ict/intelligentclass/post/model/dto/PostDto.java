package org.ict.intelligentclass.post.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class PostDto {
    private String title;
    private String content;
    private Date postTime;
    private String nickname;
    private String subCategoryName;
    private Long likeCount;
    private Long commentCount;
    private Long postViewCount;

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
