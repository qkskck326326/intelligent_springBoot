//package org.ict.intelligentclass.lecture_packages.jpa.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//@Entity
//@Table(name = "TB_PACKAGE_TECH_STACK")
//public class PackageTechStackEntity {
//
//    @EmbeddedId
//    private PackageTechStackId id;
//
//    @ManyToOne
//    @MapsId("lecturePackageId")
//    @JoinColumn(name = "LECTURE_PACKAGE_ID", insertable = false, updatable = false)
//    private LecturePackageEntity lecturePackage;
//
//    @ManyToOne
//    @MapsId("techStackId")
//    @JoinColumn(name = "TECH_STACK_ID", insertable = false, updatable = false)
//    private TechStackEntity techStack;
//}