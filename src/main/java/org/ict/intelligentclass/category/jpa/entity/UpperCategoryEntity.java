package org.ict.intelligentclass.category.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_UPPER_CATEGORY")
public class UpperCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UPPER_CATEGORY_ID")
    private Long id;

    @Column(name = "UPPER_CATEGORY_NAME")
    private String name;

    @OneToMany(mappedBy = "upperCategory")
    private Set<SubCategoryEntity> subCategories;
}
