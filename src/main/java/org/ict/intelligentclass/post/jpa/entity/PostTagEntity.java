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
    @EmbeddedId
    private PostTagId id;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "POST_ID")
    private PostEntity post;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "TAG_ID")
    private TagEntity tag;
}


