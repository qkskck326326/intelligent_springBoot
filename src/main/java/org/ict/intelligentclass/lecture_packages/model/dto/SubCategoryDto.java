//package org.ict.intelligentclass.lecture_packages.model.dto;
//
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class SubCategoryDto {
//    private Long id;
//    private String name;
//    private Long upperCategoryId;
//
//    public static SubCategoryDto fromEntity(SubCategoryEntity entity) {
//        return SubCategoryDto.builder()
//                .id(entity.getId())
//                .name(entity.getName())
//                .upperCategoryId(entity.getUpperCategoryId())
//                .build();
//    }
//
//    public SubCategoryEntity toEntity() {
//        return SubCategoryEntity.builder()
//                .id(this.id)
//                .name(this.name)
//                .upperCategoryId(this.upperCategoryId)
//                .build();
//    }
//}