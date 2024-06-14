package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_LIKE")
public class LikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_LIKE_ID")
    @SequenceGenerator(name = "SQ_LIKE_ID", sequenceName = "SQ_LIKE_ID", allocationSize = 1)
    @Column(name = "LIKE_ID")
    private Long id;

    @Column(name = "POST_ID", nullable = false)
    private Long postId;

    @Column(name = "USEREMAIL", nullable = false, length = 50)
    private String userEmail;

    @Column(name = "PROVIDER", nullable = false, length = 20)
    private String provider;
}
