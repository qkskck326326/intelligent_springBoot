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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SUB_CATEGORY_ID")
    @SequenceGenerator(name = "SQ_SUB_CATEGORY_ID" , sequenceName = "SQ_SUB_CATEGORY_ID", allocationSize = 1)
    @Column(name = "SUB_CATEGORY_ID")
    private Long id;

    @Column(name = "SUB_CATEGORY_NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "UPPER_CATEGORY_ID")
    private UpperCategoryEntity upperCategory;

//    public SubCategoryDto toDto() {
//        return SubCategoryDto.builder()
//                .subCategoryId(this.subCategoryId)
//                .subCategoryName(this.subCategoryName)
//                .upperCategoryId(this.upperCategory != null ? this.upperCategory.getUpperCategoryId() : null)
//                .build();
//    }


}

