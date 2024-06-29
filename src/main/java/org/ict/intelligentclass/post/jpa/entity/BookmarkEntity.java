package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_BOOKMARK")
public class BookmarkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_BOOKMARK_ID")
    @SequenceGenerator(name = "SQ_BOOKMARK_ID", sequenceName = "SQ_BOOKMARK_ID", allocationSize = 1)
    @Column(name = "BOOKMARK_ID")
    private Long id;

    @Column(name = "POST_ID", nullable = false)
    private Long postId;

    @Column(name = "USEREMAIL", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "PROVIDER", nullable = false, length = 20)
    private String provider;

    @Column(name = "BOOKMARK_DATE", nullable = false, length = 4000)
    private LocalDateTime bookmarkDate;

}
