//package org.ict.intelligentclass.lecture_packages.model.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.ict.intelligentclass.lecture_packages.jpa.entity.UpperCategoryEntity;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class UpperCategoryDto {
//    private Long id;
//    private String name;
//
//    public static UpperCategoryDto fromEntity(UpperCategoryEntity entity) {
//        return UpperCategoryDto.builder()
//                .id(entity.getId())
//                .name(entity.getName())
//                .build();
//    }
//
//    public UpperCategoryEntity toEntity() {
//        return UpperCategoryEntity.builder()
//                .id(this.id)
//                .name(this.name)
//                .build();
//    }
//}