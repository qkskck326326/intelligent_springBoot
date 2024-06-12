package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    @Column(name = "POST_ID", nullable = false)
    private Long postId;

    @Column(name = "USEREMAIL", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "PROVIDER", nullable = false, length = 20)
    private String provider;

    @Column(name = "CONTENT", nullable = false, length = 4000)
    private String content;

    @Column(name = "COMMENT_TIME", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime commentTime;}
