package org.ict.intelligentclass.lecture_packages.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_UPPER_CATEGORY")
public class UpperCategoryEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_UPPER_CATEGORY_ID")
    @SequenceGenerator(name = "SQ_UPPER_CATEGORY_ID" , sequenceName = "SQ_UPPER_CATEGORY_ID", allocationSize = 1)
    @Column(name = "UPPER_CATEGORY_ID")
    private Long id;

    @Column(name = "UPPER_CATEGORY_NAME")
    private String name;

    @OneToMany(mappedBy = "upperCategory")
    private Set<SubCategoryEntity> subCategories;
}
