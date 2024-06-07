package org.ict.intelligentclass.lecture_packages.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubCategoryDto {
    private Long subCategoryId;
    private String subCategoryName;
    private Long upperCategoryId;

    public static SubCategoryDto fromEntity(SubCategoryEntity entity) {
        return SubCategoryDto.builder()
                .subCategoryId(entity.getSubCategoryId())
                .subCategoryName(entity.getSubCategoryName())
                .upperCategoryId(entity.getUpperCategoryId())
                .build();
    }

    public SubCategoryEntity toEntity() {
        return SubCategoryEntity.builder()
                .subCategoryId(this.subCategoryId)
                .subCategoryName(this.subCategoryName)
                .upperCategoryId(this.upperCategoryId)
                .build();
    }
}