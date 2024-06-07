package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.category.jpa.entity.SubCategoryEntity;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="TB_POST")
public class PostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

//    @ManyToOne
//    @JoinColumns({
//            @JoinColumn(name = "USER_EMAIL", referencedColumnName = "USER_EMAIL"),
//            @JoinColumn(name = "PROVIDER", referencedColumnName = "PROVIDER")
//    })
//    private User user;

    @ManyToOne
    @JoinColumn(name = "SUB_CATEGORY_ID")
    private SubCategoryEntity subCategory;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "POST_TIME")
    private Date postTime;

    @Column(name = "POST_VIEW_COUNT")
    private int viewCount;

    @OneToMany(mappedBy = "post")
    private Set<CommentEntity> comments;

    @OneToMany(mappedBy = "post")
    private Set<LikeEntity> likes;

//    public PostDto toDto() {
//        return new PostDto(
//                this.title,
//                this.content,
//                this.postTime,
//                this.user.getNickname(),
//                this.subCategory.getName(),
//                this.likes != null ? (long) this.likes.size() : 0,
//                this.comments != null ? (long) this.comments.size() : 0,
//                (long) this.viewCount
//        );
//    }
}
