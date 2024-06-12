package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;

import java.time.LocalDateTime;
import java.util.Date;
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
