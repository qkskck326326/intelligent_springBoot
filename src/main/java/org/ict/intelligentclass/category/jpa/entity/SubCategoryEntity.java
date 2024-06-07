package org.ict.intelligentclass.category.jpa.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TB_SUB_CATEGORY")
public class SubCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUB_CATEGORY_ID")
    private Long id;

    @Column(name = "SUB_CATEGORY_NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "UPPER_CATEGORY_ID")
    private UpperCategoryEntity upperCategory;
}
