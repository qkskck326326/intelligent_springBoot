package org.ict.intelligentclass.lecture_packages.model.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.ict.intelligentclass.lecture.jpa.repository.RatingRepository;
import org.ict.intelligentclass.lecture_packages.jpa.entity.*;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;
import org.ict.intelligentclass.lecture_packages.jpa.output.SubCategoryAll;
import org.ict.intelligentclass.lecture_packages.jpa.repository.LecturePackageRepository;
import org.ict.intelligentclass.lecture_packages.jpa.repository.PackageSubCategoryRepository;
import org.ict.intelligentclass.lecture_packages.jpa.repository.PackageTechStackRepository;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class LecturePackageService {

    private final LecturePackageRepository lecturePackageRepository;
    private final PackageSubCategoryRepository packageSubCategoryRepository;
    private final PackageTechStackRepository packageTechStackRepository;
    private final RatingRepository ratingRepository;



    public List<LecturePackageList> getAllLecturePackages() {
        List<LecturePackageEntity> lecturePackages = lecturePackageRepository.findAllSort();
        return lecturePackages.stream().map(this::toLecturePackageList).collect(Collectors.toList());
    }


    private LecturePackageList toLecturePackageList(LecturePackageEntity lecturePackage) {
        RatingEntity rating = ratingRepository.findByLecturePackageId(lecturePackage.getLecturePackageId());

        return LecturePackageList.builder()
                .lecturePackageId(lecturePackage.getLecturePackageId())
                .nickname(lecturePackage.getNickname())
                .title(lecturePackage.getTitle())
                .thumbnail(lecturePackage.getThumbnail())
                .viewCount(lecturePackage.getViewCount())
                .ratingId(rating != null ? rating.getRatingId() : 0)
                .rating(rating != null ? rating.getRating() : 0.0f)
                .build();
    }


    public LecturePackageDetail getLecturePackageDetail(Long lecturePackageId) {
        LecturePackageEntity lecturePackage = lecturePackageRepository.findById(lecturePackageId).orElse(null);


        List<PackageSubCategoryEntity> packageSubCategories = packageSubCategoryRepository.findByLecturePackageId(lecturePackageId);
        List<PackageTechStackEntity> packageTechStacks = packageTechStackRepository.findByLecturePackageId(lecturePackageId);




        List<Long> subCategoryIds = packageSubCategories.stream()
                .map(subCategory -> subCategory.getSubCategory().getId())
                .collect(Collectors.toList());

        List<Long> techStackIds = packageTechStacks.stream()
                .map(techStack -> techStack.getTechStack().getTechStackId())
                .collect(Collectors.toList());



        //패키지마다의 서브카테고리의 일치하는 서브카테고리entity에서 map으로 name을 꺼냄.
        String subCategoryNames = packageSubCategories.stream()
                .map(subCategory -> subCategory.getSubCategory().getName())
                .collect(Collectors.joining(", "));

        String techStackPaths = packageTechStacks.stream()
                .map(techStack -> techStack.getTechStack().getTechStackPath())
                .collect(Collectors.joining(", "));



            return LecturePackageDetail.builder()
                    .lecturePackageId(lecturePackageId)
                    .nickname(lecturePackage.getNickname())
                    .title(lecturePackage.getTitle())
                    .classGoal(lecturePackage.getClassGoal())
                    .recommendPerson(lecturePackage.getRecommendPerson())
                    .priceKind(lecturePackage.getPriceKind())
                    .price(lecturePackage.getPrice())
                    .thumbnail(lecturePackage.getThumbnail())
                    .register(lecturePackage.getRegisterDate())
                    .viewCount(lecturePackage.getViewCount())
                    .subCategoryId(subCategoryIds)
                    .subCategoryName(subCategoryNames)
                    .techStackId(techStackIds)
                    .techStackPath(techStackPaths)
                    .build();
    }










//    public LecturePackageDetail getLecturePackage(Long lecturePackageId) {
//        LecturePackageEntity lecturePackageEntity = lecturePackageRepository.findById(lecturePackageId)
//                .orElseThrow(() -> new RuntimeException("패키지를 찾을 수 없습니다."));
//
//        // 서브 카테고리 및 기술 스택 엔티티를 조회하여 DTO로 변환
//        List<PackageSubCategoryId> packageSubCategoryIds = lecturePackageEntity.getPackageSubCategory().stream()
//                .map(PackageSubCategoryEntity::getPackageSubCategoryId)
//                .collect(Collectors.toList());
//
//        List<PackageTechStackId> packageTechStackIds = lecturePackageEntity.getPackageTechStack().stream()
//                .map(PackageTechStackEntity::getPackageTechStackId)
//                .collect(Collectors.toList());
//
//        // 서브 카테고리 이름과 기술 스택 경로를 조회
//        String subCategoryNames = lecturePackageEntity.getPackageSubCategory().stream()
//                .map(subCategory -> subCategory.getSubCategory().getSubCategoryName())
//                .collect(Collectors.joining(", "));
//
//        String techStackPaths = lecturePackageEntity.getPackageTechStack().stream()
//                .map(techStack -> techStack.getTechStack().getPath())
//                .collect(Collectors.joining(", "));
//
//        return LecturePackageDetail.builder()
//                .lecturePackageId(lecturePackageEntity.getLecturePackageId())
//                .nickname(lecturePackageEntity.getNickname())
//                .title(lecturePackageEntity.getTitle())
//                .classGoal(lecturePackageEntity.getClassGoal())
//                .recommendPerson(lecturePackageEntity.getRecommendPerson())
//                .priceKind(lecturePackageEntity.getPriceKind())
//                .price(lecturePackageEntity.getPrice())
//                .thumbnail(lecturePackageEntity.getThumbnail())
//                .register(lecturePackageEntity.getRegisterDate())
//                .viewCount(lecturePackageEntity.getViewCount())
//                .ratingId(lecturePackageEntity.getRating().getRatingId())
//                .rating(lecturePackageEntity.getRating().getRating())
//                .packageSubCategoryIds(packageSubCategoryIds)
//                .subCategoryName(subCategoryNames)
//                .packageTechStackIds(packageTechStackIds)
//                .path(techStackPaths)
//                .build();
//    }



//    public Map<String, Object> getLecturePackages() {
//
//        List<LecturePackageEntity> lecturePackages = lecturePackageRepository.findAllSort();
//        List<RatingEntity> ratings = ratingRepository.findAll();
//
//        log.info("Lecture Packages: {}", lecturePackages);
//
////        //dto객체 생성
////        List<LecturePackageList> lecturePackage = lecturePackages.stream()
////                .map(LecturePackageList::new)
////                .collect(Collectors.toList());
//
//
//
//        return lecturePackages.map(package -> {
//            LecturePackageList list = new LecturePackageList();
//            list.setLecturePackageId(lecturePackages.getPackage)
//
//        });
//    }





//    @Transactional
//    public LecturePackageDto createLecturePackage(LecturePackageDto lecturePackageDto) {
//        // dto객체를 entity 객체로 변환함.
//        LecturePackageEntity lecturePackageEntity = lecturePackageDto.toEntity();
//
//        //entity객체를 데이터베이스에 저장하고, 저장된 객체를 다시 반환받아 ID가 설정된 lecturePackageEntity로 업데이트됨.
//        lecturePackageEntity = lecturePackageRepository.save(lecturePackageEntity);
//
//        // lecturePackageEntity를 final 변수로 저장하여, 람다 표현식 내에서 사용가능하게함.
//        final LecturePackageEntity savedLecturePackageEntity = lecturePackageEntity;
//
//
//        // 패키지마다의 하위카테고리 저장.
//        lecturePackageDto.getPackageSubCategories().forEach(packageSubCategoryDto -> { //lecturePackageDto에서 하위카테고리 목록을 가져와 각 항목을 처리함.
//
//            // PackageSubCategoryEntity객체 생성.
//            PackageSubCategoryEntity packageSubCategoryEntity = new PackageSubCategoryEntity();
//            //복합키 객체를 생성하여, 위에서 저장해둔 lecturePackage엔티티 객체와 하위카테고리의 dto에서 id를 가져와 PackageSubCategory의 복합키로 넣어줌.
//            packageSubCategoryEntity.setId(new PackageSubCategoryId(savedLecturePackageEntity.getLecturePackageId(), packageSubCategoryDto.getPackageSubCategoryId().getSubCategoryId()));
//            //강의 패키지엔티티 저장.
//            packageSubCategoryEntity.setLecturePackage(savedLecturePackageEntity);
//            //패키지마다의 카테고리에서
//            packageSubCategoryEntity.setSubCategory(packageSubCategoryDto.getSubCategory().toEntity());
//            packageSubCategoryRepository.save(packageSubCategoryEntity);
//        });



//        // 패키지마다의 기술스택 저장.
//        lecturePackageDto.getPackageTechStacks().forEach(packageTechStackDto -> {
//            PackageTechStackEntity techStackEntity = new PackageTechStackEntity();
//            techStackEntity.setId(new PackageTechStackId(savedLecturePackageEntity.getLecturePackageId(), packageTechStackDto.getPackageTechStackId().getTechStackId()));
//            techStackEntity.setLecturePackage(savedLecturePackageEntity);
//            techStackEntity.setTechStack(packageTechStackDto.getTechStack().toEntity());
//            packageTechStackRepository.save(techStackEntity);
//        });
//
//        // Convert Entity back to DTO using the method in DTO
//        return LecturePackageDto.fromEntity(savedLecturePackageEntity);
//    }
}