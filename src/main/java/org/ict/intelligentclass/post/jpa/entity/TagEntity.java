package org.ict.intelligentclass.post.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="TB_TAG")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TAG_ID")
    @SequenceGenerator(name = "SQ_TAG_ID", sequenceName = "SQ_TAG_ID", allocationSize = 1)
    @Column(name = "TAG_ID")
    private Long id;

    @Column(name = "TAG_NAME", nullable = false, unique = true)
    private String name;
}
