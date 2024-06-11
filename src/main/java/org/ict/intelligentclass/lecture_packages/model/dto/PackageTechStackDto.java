//package org.ict.intelligentclass.lecture_packages.model.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageSubCategoryEntity;
//import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageSubCategoryId;
//import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageTechStackEntity;
//import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageTechStackId;
//
//import static org.ict.intelligentclass.lecture_packages.jpa.entity.QPackageTechStackId.packageTechStackId;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class PackageTechStackDto {
//    private Long lecturePackageId;
//    private Long techStackId;
//
//    public PackageTechStackEntity toEntiry(){
//        PackageTechStackId packageTechStackId = new PackageTechStackId(lecturePackageId, techStackId);
//        return PackageTechStackEntity.builder()
//                .packageTechStackId(packageTechStackId)
//                .build();
//    }
//
//}