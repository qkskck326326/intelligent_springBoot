//package org.ict.intelligentclass.lecture_packages.model.dto;
//
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.ict.intelligentclass.lecture_packages.jpa.entity.TechStackEntity;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class TechStackDto {
//    private Long id;
//    private String path;
//    private String name;
//
//    public static TechStackDto fromEntity(TechStackEntity entity) {
//        return TechStackDto.builder()
//                .id(entity.getId())
//                .path(entity.getPath())
//                .name(entity.getName())
//                .build();
//    }
//
//    public TechStackEntity toEntity() {
//        return TechStackEntity.builder()
//                .id(this.id)
//                .path(this.path)
//                .name(this.name)
//                .build();
//    }
//}