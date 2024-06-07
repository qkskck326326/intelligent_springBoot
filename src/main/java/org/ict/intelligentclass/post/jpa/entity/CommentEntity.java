package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_COMMENT")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private PostEntity post;

//    @ManyToOne
//    @JoinColumns({
//            @JoinColumn(name = "USER_EMAIL", referencedColumnName = "USER_EMAIL"),
//            @JoinColumn(name = "PROVIDER", referencedColumnName = "PROVIDER")
//    })
//    private User user;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "COMMENT_TIME")
    private Date commentTime;
}
