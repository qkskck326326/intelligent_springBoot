package org.ict.intelligentclass.lecture_packages.jpa.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.model.dto.SubCategoryDto;


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
    private Long subCategoryId;

    @Column(name = "SUB_CATEGORY_NAME", nullable = false, length = 30)
    private String subCategoryName;

    @ManyToOne
    @JoinColumn(name = "UPPER_CATEGORY_ID", nullable = false)
    private UpperCategoryEntity upperCategory;

    public SubCategoryDto toDto() {
        return SubCategoryDto.builder()
                .subCategoryId(this.subCategoryId)
                .subCategoryName(this.subCategoryName)
                .upperCategoryId(this.upperCategory != null ? this.upperCategory.getUpperCategoryId() : null)
                .build();
    }


}

