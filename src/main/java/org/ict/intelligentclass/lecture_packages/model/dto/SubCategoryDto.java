package org.ict.intelligentclass.lecture_packages.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.UpperCategoryEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubCategoryDto {
    private Long subCategoryId;
    private String subCategoryName;
    private Long upperCategoryId;

//    public SubCategoryEntity toEntity() {
//        UpperCategoryEntity upperCategoryEntity = UpperCategoryEntity.builder()
//                .upperCategoryId(this.upperCategoryId)
//                .build();
//
//        return SubCategoryEntity.builder()
//                .subCategoryId(this.subCategoryId)
//                .subCategoryName(this.subCategoryName)
//                .upperCategory(upperCategoryEntity)
//                .build();
//    }
}