package org.ict.intelligentclass.lecture_packages.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.UpperCategoryEntity;

import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpperCategoryDto {
    private Long upperCategoryId;
    private String upperCategoryName;


    public UpperCategoryEntity toEntity() {
        return UpperCategoryEntity.builder()
                .upperCategoryId(this.upperCategoryId)
                .upperCategoryName(this.upperCategoryName)
                .build();
    }
}