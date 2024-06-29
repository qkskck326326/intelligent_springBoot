package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostTagId implements java.io.Serializable {
    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "TAG_ID")
    private Long tagId;
}
