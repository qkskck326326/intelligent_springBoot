//package org.ict.intelligentclass.lecture_packages.model.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageSubCategoryEntity;
//import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageSubCategoryId;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class PackageSubCategoryDto {
//    private PackageSubCategoryIdDto id;
//    private SubCategoryDto subCategory;
//
//    public static PackageSubCategoryDto fromEntity(PackageSubCategoryEntity entity) {
//        return PackageSubCategoryDto.builder()
//                .id(new PackageSubCategoryIdDto(entity.getId().getLecturePackageId(), entity.getId().getSubCategoryId()))
//                .subCategory(SubCategoryDto.fromEntity(entity.getSubCategory()))
//                .build();
//    }
//
//    public PackageSubCategoryEntity toEntity() {
//        PackageSubCategoryEntity entity = new PackageSubCategoryEntity();
//        entity.setId(new PackageSubCategoryId(this.id.getLecturePackageId(), this.id.getSubCategoryId()));
//        entity.setSubCategory(this.subCategory.toEntity());
//        return entity;
//    }
//}