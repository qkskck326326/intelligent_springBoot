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
@Table(name = "TB_UPPER_CATEGORY")
public class UpperCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UPPER_CATEGORY_ID")
    private Long upperCategoryId;

    @Column(name = "UPPER_CATEGORY_NAME", nullable = false)
    private String upperCategoryName;
}
