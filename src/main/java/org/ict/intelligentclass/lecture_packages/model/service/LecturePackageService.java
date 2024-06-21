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
import org.ict.intelligentclass.lecture_packages.jpa.repository.LecturePackageRepository;
import org.ict.intelligentclass.lecture_packages.jpa.repository.PackageSubCategoryRepository;
import org.ict.intelligentclass.lecture_packages.jpa.repository.PackageTechStackRepository;
import org.ict.intelligentclass.lecture_packages.jpa.repository.UpperCategoryRepository;
import org.ict.intelligentclass.user.jpa.entity.UserInterestEntity;
import org.ict.intelligentclass.user.jpa.repository.UserInterestRepository;
import org.springframework.data.domain.*;
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
    private final UserInterestRepository userInterestRepository;
    private final UpperCategoryRepository upperCategoryRepository;




    //모든 패키지리스트 조회

    @Transactional
    public Page<LecturePackageList> getAllLecturePackages(int page, int size, String sortCriteria, String searchTerm, Long subCategoryId, String searchCriteria) {
        Pageable pageable = PageRequest.of(page, size, getSort(sortCriteria));
        Page<LecturePackageEntity> lecturePackageEntities = null;

        if (subCategoryId != null) {
            if ("rating".equals(sortCriteria)) {
                lecturePackageEntities = lecturePackageRepository.findBySubCategoryIdOrderByRating(subCategoryId, pageable);
            } else {
                lecturePackageEntities = lecturePackageRepository.findBySubCategoryId(subCategoryId, pageable);
            }
        } else if ("rating".equals(sortCriteria)) {
            lecturePackageEntities = lecturePackageRepository.findAllOrderByRating(pageable);
        } else if (searchCriteria != null){
            if ("title".equals(searchCriteria)) {
                lecturePackageEntities = lecturePackageRepository.searchByTitle(searchTerm, pageable);
            } else if ("nickname".equals(searchCriteria)) {
                lecturePackageEntities = lecturePackageRepository.searchByInstructor(searchTerm, pageable);
            }

        }else{
            lecturePackageEntities = lecturePackageRepository.findAll(pageable);
        }

        List<LecturePackageList> lecturePackageLists = lecturePackageEntities.stream()
                .map(this::toLecturePackageList)
                .collect(Collectors.toList());

        return new PageImpl<>(lecturePackageLists, pageable, lecturePackageEntities.getTotalElements());
    }

    private Sort getSort(String sortCriteria) {
        if (sortCriteria == null || sortCriteria.isEmpty()) {
            return Sort.by("registerDate").descending();
        }
        switch (sortCriteria) {
            case "viewCount":
                return Sort.by("viewCount").descending();
            case "latest":
            default:
                return Sort.by("registerDate").descending();
        }
    }



    private LecturePackageList toLecturePackageList(LecturePackageEntity lecturePackage) {
        RatingEntity rating = ratingRepository.findByLecturePackageId(lecturePackage.getLecturePackageId());

        return LecturePackageList.builder()
                .lecturePackageId(lecturePackage.getLecturePackageId())
                .nickname(lecturePackage.getNickname())
                .title(lecturePackage.getTitle())
                .thumbnail(lecturePackage.getThumbnail())
                .viewCount(lecturePackage.getViewCount())
                .packageLevel(lecturePackage.getPackageLevel())
                .registerDate(lecturePackage.getRegisterDate())
                .ratingId(rating != null ? rating.getRatingId() : 0)
                .rating(rating != null ? rating.getRating() : 0.0f)
                .build();
    }



    // 카테고리 별 패키지 리스트 조회하기
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


    // 유저관심패키지 TOP10
    @Transactional
    public List<LecturePackageList> getInterestPackages(String email, String provider) {
        // 유저의 관심 카테고리 ID 목록을 가져옵니다.
        List<UserInterestEntity> userInterests = userInterestRepository.findByInterestIdUserEmailAndInterestIdProvider(email, provider);
        List<Long> subCategoryIds = userInterests.stream()
                .map(interest -> interest.getSubCategory().getId())
                .collect(Collectors.toList());

        log.info("userInterests:", userInterests);

        // 관심 카테고리에 해당하는 패키지 ID를 가져옵니다.
        List<PackageSubCategoryEntity> packageSubCategories = packageSubCategoryRepository.findBySubCategoryIdIn(subCategoryIds);
        List<Long> lecturePackageIds = packageSubCategories.stream()
                .map(packageSubCategory -> packageSubCategory.getLecturePackage().getLecturePackageId())
                .collect(Collectors.toList());

        log.info("packageSubCategories:", packageSubCategories);

        // 해당 패키지들을 가져옵니다.
        List<LecturePackageEntity> lecturePackages = lecturePackageRepository.findByLecturePackageIdIn(lecturePackageIds);

        log.info("lecturePackages:", lecturePackages);

        // 해당 패키지들의 별점을 가져옵니다.
        List<RatingEntity> ratings = ratingRepository.findByLecturePackageIdIn(lecturePackageIds);

        log.info("ratings:", ratings);

        // 패키지 ID를 키로, 해당 패키지의 평균 별점을 값으로 하는 맵을 만듭니다.
        Map<Long, Double> packageRatings = ratings.stream()
                .collect(Collectors.groupingBy(
                        RatingEntity::getLecturePackageId,
                        Collectors.averagingDouble(RatingEntity::getRating)
                ));

        log.info("packageRatings:", packageRatings);

        // LecturePackageEntity를 LecturePackageList로 변환하고, 별점 순으로 정렬합니다.
        return lecturePackages.stream()
                .map(packageEntity -> {
                    double rating = packageRatings.getOrDefault(packageEntity.getLecturePackageId(), 0.0);
                    return LecturePackageList.builder()
                            .lecturePackageId(packageEntity.getLecturePackageId())
                            .nickname(packageEntity.getNickname())
                            .title(packageEntity.getTitle())
                            .thumbnail(packageEntity.getThumbnail())
                            .viewCount(packageEntity.getViewCount())
                            .packageLevel(packageEntity.getPackageLevel())
                            .rating((float) rating)
                            .build();
                })
                .sorted(Comparator.comparing(LecturePackageList::getRating).reversed())
                .limit(12)
                .collect(Collectors.toList());
    }




    // 모든 상위카테고리별 패키지 리스트 TOP4
    public Map<Long, List<LecturePackageList>> getUpperCategoryPackageAll() {
        // 상위카테고리가 키인 구조로 리턴할 map을 생성함.
        Map<Long, List<LecturePackageList>> result = new HashMap<>();

        //1. 상위카테고리 모두 가져옴
        List<UpperCategoryEntity> upperCategories = upperCategoryRepository.findAll();

        //2. 상위카테고리별 패키지 정보 가져오기.
        // 각 상위 카테고리에 속하는 서브 카테고리 & 해당 패키지id를 가져옴.
        for (UpperCategoryEntity upperCategory : upperCategories) {
            List<PackageSubCategoryEntity> packageSubCategories = packageSubCategoryRepository.findBySubCategoryUpperCategoryId(upperCategory.getId());

            // 서브 카테고리별로 패키지 ID를 키로 하여 그룹화함.
            Map<Long, List<PackageSubCategoryEntity>> packageMap = packageSubCategories.stream()
                    .collect(Collectors.groupingBy(p -> p.getLecturePackage().getLecturePackageId()));

            // 각 서브 카테고리에 속하는 패키지를 가져옴.
            List<LecturePackageEntity> lecturePackages = packageMap.values().stream()
                    .map(list -> list.get(0).getLecturePackage())
                    .distinct()
                    .collect(Collectors.toList());


            // 3. 패키지의 별점 정보 가져옴.
            // 패키지 ID 목록을 추출하고 해당 패키지의 별점 정보를 가져옴.
            List<Long> lecturePackageIds = lecturePackages.stream()
                    .map(LecturePackageEntity::getLecturePackageId)
                    .collect(Collectors.toList());

            List<RatingEntity> ratings = ratingRepository.findByLecturePackageIdIn(lecturePackageIds);


            // 패키지id를 키로 하여 평균 별점을 맵으로 만듦.
            Map<Long, Double> packageRatings = ratings.stream()
                    .collect(Collectors.groupingBy(
                            RatingEntity::getLecturePackageId,
                            Collectors.averagingDouble(RatingEntity::getRating)
                    ));


            // 4. 패키지별 별점 순으로 정렬 및 상위 4개 패키지 추출함.
            // 패키지 정보를 LecturePackageList 형식으로 변환하고 별점 순으로 정렬하여 상위 4개의 패키지만 추출함.
            List<LecturePackageList> sortedPackages = lecturePackages.stream()
                    .map(packageEntity -> {
                        double rating = packageRatings.getOrDefault(packageEntity.getLecturePackageId(), 0.0);
                        return LecturePackageList.builder()
                                .lecturePackageId(packageEntity.getLecturePackageId())
                                .nickname(packageEntity.getNickname())
                                .title(packageEntity.getTitle())
                                .thumbnail(packageEntity.getThumbnail())
                                .viewCount(packageEntity.getViewCount())
                                .packageLevel(packageEntity.getPackageLevel())
                                .rating((float) rating)
                                .build();
                    })
                    .sorted(Comparator.comparing(LecturePackageList::getRating).reversed())
                    .limit(4)
                    .collect(Collectors.toList());

            result.put(upperCategory.getId(), sortedPackages);
        }

        return result;
    }








    //강의패키지 상세보기
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
                .backgroundColor(lecturePackage.getBackgroundColor())
                .subCategoryId(subCategoryIds)
                .subCategoryName(subCategoryNames)
                .techStackId(techStackIds)
                .techStackPath(techStackPaths)
                .build();
    }


    // 강의패키지 등록하기
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
                .backgroundColor(register.getBackgroundColor())
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






    // 강의패키지 수정하기
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
        existingEntity.setBackgroundColor(lecturePackageRegister.getBackgroundColor());

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



    //강의패키지 삭제하기
    @Transactional
    public void deleteLecturePackage(Long lecturePackageId) {
        packageSubCategoryRepository.deleteAllByPackageSubCategoryId_LecturePackageId(lecturePackageId);
        packageTechStackRepository.deleteAllByPackageTechStackId_LecturePackageId(lecturePackageId);
        lecturePackageRepository.deleteById(lecturePackageId);
    }


    //조회수 +1증가처리
    @Transactional
    public void incrementViewCount(Long lecturePackageId) {
        Optional<LecturePackageEntity> lecturePackageOpt = lecturePackageRepository.findById(lecturePackageId);
        if (lecturePackageOpt.isPresent()) {
            LecturePackageEntity lecturePackage = lecturePackageOpt.get();
            lecturePackage.setViewCount(lecturePackage.getViewCount() + 1);
            lecturePackageRepository.save(lecturePackage);
        }
    }






}





