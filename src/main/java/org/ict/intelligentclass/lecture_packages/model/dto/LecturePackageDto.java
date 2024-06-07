//package org.ict.intelligentclass.lecture_packages.model.dto;
//
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.ict.intelligentclass.lecture_packages.jpa.entity.*;
//
//import java.util.Date;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class LecturePackageDto {
//    private Long id;
//    private String nickname;
//    private String title;
//    private String classGoal;
//    private String recommendPerson;
//    private int priceKind;
//    private int price;
//    private String thumbnail;
//    @JsonFormat(pattern = "yyyy-MM-dd")
//    private Date registerDate;
//    private int viewCount;
//    private Set<PackageSubCategoryDto> subCategories;
//    private Set<PackageTechStackDto> techStacks;
//
//    public static LecturePackageDto fromEntity(LecturePackageEntity entity) {
//        return LecturePackageDto.builder()
//                .id(entity.getId())
//                .nickname(entity.getNickname())
//                .title(entity.getTitle())
//                .classGoal(entity.getClassGoal())
//                .recommendPerson(entity.getRecommendPerson())
//                .priceKind(entity.getPriceKind())
//                .price(entity.getPrice())
//                .thumbnail(entity.getThumbnail())
//                .registerDate(entity.getRegisterDate())
//                .viewCount(entity.getViewCount())
//                .subCategories(entity.getSubCategories().stream()
//                        .map(PackageSubCategoryDto::fromEntity)
//                        .collect(Collectors.toSet()))
//                .techStacks(entity.getTechStacks().stream()
//                        .map(PackageTechStackDto::fromEntity)
//                        .collect(Collectors.toSet()))
//                .build();
//    }
//
//    public LecturePackageEntity toEntity() {
//        LecturePackageEntity entity = LecturePackageEntity.builder()
//                .id(this.id)
//                .nickname(this.nickname)
//                .title(this.title)
//                .classGoal(this.classGoal)
//                .recommendPerson(this.recommendPerson)
//                .priceKind(this.priceKind)
//                .price(this.price)
//                .thumbnail(this.thumbnail)
//                .registerDate(this.registerDate)
//                .viewCount(this.viewCount)
//                .build();
//        return entity;
//    }
//}