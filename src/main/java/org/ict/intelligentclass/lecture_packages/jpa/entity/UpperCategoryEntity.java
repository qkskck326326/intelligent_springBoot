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

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "UPPER_CATEGORY_ID")
//    private Long upperCategoryId;
//
//    @Column(name = "UPPER_CATEGORY_NAME", nullable = false, length = 30)
//    private String upperCategoryName;
//
//    @OneToMany(mappedBy = "upperCategory")
//    private Set<SubCategoryEntity> subCategory;

//    public UpperCategoryDto toDto() {
//        return UpperCategoryDto.builder()
//                .upperCategoryId(this.upperCategoryId)
//                .upperCategoryName(this.upperCategoryName)
//                .build();
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UPPER_CATEGORY_ID")
    private Long id;

    @Column(name = "UPPER_CATEGORY_NAME")
    private String name;

    @OneToMany(mappedBy = "upperCategory")
    private Set<SubCategoryEntity> subCategories;
}
