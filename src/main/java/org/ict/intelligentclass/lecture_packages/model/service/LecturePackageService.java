package org.ict.intelligentclass.lecture_packages.model.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.ict.intelligentclass.lecture.jpa.repository.RatingRepository;
import org.ict.intelligentclass.lecture_packages.jpa.entity.*;
import org.ict.intelligentclass.lecture_packages.jpa.input.LecturePackageRegister;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;
import org.ict.intelligentclass.lecture_packages.jpa.output.SubCategoryAll;
import org.ict.intelligentclass.lecture_packages.jpa.repository.LecturePackageRepository;
import org.ict.intelligentclass.lecture_packages.jpa.repository.PackageSubCategoryRepository;
import org.ict.intelligentclass.lecture_packages.jpa.repository.PackageTechStackRepository;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class LecturePackageService {

    private final LecturePackageRepository lecturePackageRepository;
    private final PackageSubCategoryRepository packageSubCategoryRepository;
    private final PackageTechStackRepository packageTechStackRepository;
    private final RatingRepository ratingRepository;
    private final PackageSubCategoryRepository packageSubcategoryRepository;



    //모든 패키지리스트 조회
    @Transactional
    public List<LecturePackageList> getAllLecturePackages() {
        List<LecturePackageEntity> lecturePackages = lecturePackageRepository.findAllSort();
        return lecturePackages.stream().map(this::toLecturePackageList).collect(Collectors.toList());
    }


    //패키지 리스트 조회 시 필요함.
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


    // 카테고리 버튼 클릭 시 카테고리에 해당하는 패키지 리스트 조회
    @Transactional
    public List<LecturePackageList> getCategorySortedPackages(Long categoryId) {
        log.info("categoryId:", categoryId);
        // 카테고리로 해당하는 패키지 아이디를 추출함.
        List<PackageSubCategoryEntity> subCategoryEntities = packageSubCategoryRepository.categorySortPackages(categoryId);
        // 추출한 패키지 아이디를 map으로 하나하나 꺼냄.
        List<Long> packageIds = subCategoryEntities.stream()
                .map(ps -> ps.getPackageSubCategoryId().getLecturePackageId())
                .collect(Collectors.toList());

        // 추출한 패키지 아이디 리스트를 가지고 해당하는 패키지 리스트를 추출함.
        List<LecturePackageEntity> lecturePackages = lecturePackageRepository.findByLecturePackageIdIn(packageIds);
        // 패키지 아이디에 해당하는 별점 리스트와 같이 리턴해줌.
        return lecturePackages.stream().map(this::toLecturePackageList).collect(Collectors.toList());
    }







    @Transactional
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
                .content(lecturePackage.getContent())
                .priceKind(lecturePackage.getPriceKind())
                .price(lecturePackage.getPrice())
                .packageLevel(lecturePackage.getPackageLevel())
                .thumbnail(lecturePackage.getThumbnail())
                .registerDate(lecturePackage.getRegisterDate())
                .viewCount(lecturePackage.getViewCount())
                .subCategoryId(subCategoryIds)
                .subCategoryName(subCategoryNames)
                .techStackId(techStackIds)
                .techStackPath(techStackPaths)
                .build();
    }


    @Transactional
    public LecturePackageEntity createLecturePackage(LecturePackageRegister register) {
        LecturePackageEntity lecturePackage = LecturePackageEntity.builder()
                .nickname(register.getNickname())
                .title(register.getTitle())
                .content(register.getContent())
                .packageLevel(register.getPackageLevel())
                .priceKind(register.getPriceKind())
                .price(register.getPrice())
                .thumbnail(register.getThumbnail())
                .build();

        LecturePackageEntity savedLecturePackage = lecturePackageRepository.save(lecturePackage);

        // 패키지 서브카테고리 저장
        for (Long subCategoryId : register.getPackageSubCategoryId()) {
            PackageSubCategoryEntity subCategoryEntity = PackageSubCategoryEntity.builder()
                    .packageSubCategoryId(new PackageSubCategoryId(savedLecturePackage.getLecturePackageId(), subCategoryId))
                    .lecturePackage(savedLecturePackage)
                    .subCategory(SubCategoryEntity.builder().id(subCategoryId).build())
                    .build();
            packageSubCategoryRepository.save(subCategoryEntity);
        }

        // 패키지 기술스택 저장
        for (Long techStackId : register.getPackageTechStackId()) {
            PackageTechStackEntity techStackEntity = PackageTechStackEntity.builder()
                    .packageTechStackId(new PackageTechStackId(savedLecturePackage.getLecturePackageId(), techStackId))
                    .lecturePackage(savedLecturePackage)
                    .techStack(TechStackEntity.builder().techStackId(techStackId).build())
                    .build();
            packageTechStackRepository.save(techStackEntity);
        }

        return savedLecturePackage;
    }






    @Transactional
    public LecturePackageEntity modifyLecturePackage(Long lecturePackageId, LecturePackageRegister lecturePackageRegister) {
        // 1. 기존 엔티티 가져오기
        LecturePackageEntity existingEntity = lecturePackageRepository.findById(lecturePackageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid package ID"));

        // 2. 필드 업데이트
        existingEntity.setTitle(lecturePackageRegister.getTitle());
        existingEntity.setContent(lecturePackageRegister.getContent());
        existingEntity.setPackageLevel(lecturePackageRegister.getPackageLevel());
        existingEntity.setPriceKind(lecturePackageRegister.getPriceKind());
        existingEntity.setPrice(lecturePackageRegister.getPrice());
        existingEntity.setThumbnail(lecturePackageRegister.getThumbnail());

        // 3. 기존 서브카테고리 삭제
        packageSubCategoryRepository.deleteAllByPackageSubCategoryId_LecturePackageId(lecturePackageId);
        // 4. 새로운 서브카테고리 추가
        Set<PackageSubCategoryEntity> updatedSubCategories = new HashSet<>();
        for (Long subCategoryId : lecturePackageRegister.getPackageSubCategoryId()) {
            PackageSubCategoryEntity subCategoryEntity = PackageSubCategoryEntity.builder()
                    .packageSubCategoryId(new PackageSubCategoryId(lecturePackageId, subCategoryId))
                    .lecturePackage(existingEntity) // lecturePackage 필드를 올바르게 설정
                    .subCategory(SubCategoryEntity.builder().id(subCategoryId).build())
                    .build();
            updatedSubCategories.add(subCategoryEntity);
        }
        packageSubCategoryRepository.saveAll(updatedSubCategories);

        // 5. 기존 기술스택 삭제
        packageTechStackRepository.deleteAllByPackageTechStackId_LecturePackageId(lecturePackageId);
        // 6. 새로운 기술스택 추가
        Set<PackageTechStackEntity> updatedTechStacks = new HashSet<>();
        for (Long techStackId : lecturePackageRegister.getPackageTechStackId()) {
            PackageTechStackEntity techStackEntity = PackageTechStackEntity.builder()
                    .packageTechStackId(new PackageTechStackId(lecturePackageId, techStackId))
                    .lecturePackage(existingEntity) // lecturePackage 필드를 올바르게 설정
                    .techStack(TechStackEntity.builder().techStackId(techStackId).build())
                    .build();
            updatedTechStacks.add(techStackEntity);
        }
        packageTechStackRepository.saveAll(updatedTechStacks);

        // 7. 업데이트된 엔티티 저장 및 반환
        return lecturePackageRepository.save(existingEntity);
    }

    @Transactional
    public void deleteLecturePackage(Long lecturePackageId) {
        packageSubCategoryRepository.deleteAllByPackageSubCategoryId_LecturePackageId(lecturePackageId);
        packageTechStackRepository.deleteAllByPackageTechStackId_LecturePackageId(lecturePackageId);
        lecturePackageRepository.deleteById(lecturePackageId);
    }


    public void incrementViewCount(Long lecturePackageId) {
        Optional<LecturePackageEntity> lecturePackageOpt = lecturePackageRepository.findById(lecturePackageId);
        if (lecturePackageOpt.isPresent()) {
            LecturePackageEntity lecturePackage = lecturePackageOpt.get();
            lecturePackage.setViewCount(lecturePackage.getViewCount() + 1);
            lecturePackageRepository.save(lecturePackage);
        }
    }



//    public List<PackageSubCategoryEntity> getCategoryPackageId(Long categoryId){
//        return packageSubcategoryRepository.categorySortPackages(categoryId);
//    }
//
//
//    @Transactional
//    public List<LecturePackageList> getCategorySortPackages() {
//        List<LecturePackageEntity> lecturePackages = lecturePackageRepository.findAllSort();
//        return lecturePackages.stream().map(this::toLecturePackageList).collect(Collectors.toList());
//    }




}





