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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKE_ID")
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
}
