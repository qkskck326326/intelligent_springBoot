package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="TB_POST_TAG")
public class PostTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_POST_TAG_ID")
    @SequenceGenerator(name = "SQ_POST_TAG_ID", sequenceName = "SQ_POST_TAG_ID", allocationSize = 1)
    @Column(name = "POST_TAG_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "POST_ID", nullable = false)
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "TAG_ID", nullable = false)
    private TagEntity tag;
}
