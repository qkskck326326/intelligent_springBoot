package org.ict.intelligentclass.lecture_packages.jpa.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_SUB_CATEGORY")
public class SubCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUB_CATEGORY_ID")
    private Long id;

    @Column(name = "SUB_CATEGORY_NAME", nullable = false)
    private String name;

    @Column(name = "UPPER_CATEGORY_ID", nullable = false)
    private Long upperCategoryId;

    @ManyToOne
    @JoinColumn(name = "UPPER_CATEGORY_ID", insertable = false, updatable = false)
    private UpperCategoryEntity upperCategory;
}

