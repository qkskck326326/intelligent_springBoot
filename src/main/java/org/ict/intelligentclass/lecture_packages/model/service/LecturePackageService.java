package org.ict.intelligentclass.lecture_packages.model.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.ict.intelligentclass.lecture.jpa.repository.RatingRepository;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;
import org.ict.intelligentclass.lecture_packages.jpa.repository.LecturePackageRepository;
import org.ict.intelligentclass.lecture_packages.jpa.repository.PackageSubCategoryRepository;
import org.ict.intelligentclass.lecture_packages.jpa.repository.PackageTechStackRepository;
import org.springframework.stereotype.Service;



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