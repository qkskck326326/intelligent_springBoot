//
//import org.ict.intelligentclass.lecture_packages.jpa.entity.*;
//import org.ict.intelligentclass.lecture_packages.jpa.repository.*;
//import org.ict.intelligentclass.lecture_packages.model.dto.LecturePackageDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class LecturePackageService {
//
//    @Autowired
//    private LecturePackageRepository lecturePackageRepository;
//
//    @Autowired
//    private SubCategoryRepository subCategoryRepository;
//
//    @Autowired
//    private TechStackRepository techStackRepository;
//
//    @Autowired
//    private PackageSubCategoryRepository packageSubCategoryRepository;
//
//    @Autowired
//    private PackageTechStackRepository packageTechStackRepository;
//
//    @Transactional
//    public LecturePackageDto createLecturePackage(LecturePackageDto lecturePackageDto) {
//        // Convert DTO to Entity using the method in DTO
//        LecturePackageEntity lecturePackageEntity = lecturePackageDto.toEntity();
//
//        // Save Lecture Package first to generate the ID
//        lecturePackageEntity = lecturePackageRepository.save(lecturePackageEntity);
//
//        // Save Sub Categories
//        lecturePackageDto.getSubCategories().forEach(subCategoryDto -> {
//            PackageSubCategoryEntity subCategoryEntity = new PackageSubCategoryEntity();
//            subCategoryEntity.setId(new PackageSubCategoryId(lecturePackageEntity.getId(), subCategoryDto.getId().getSubCategoryId()));
//            subCategoryEntity.setLecturePackage(lecturePackageEntity);
//            subCategoryEntity.setSubCategory(subCategoryDto.getSubCategory().toEntity());
//            packageSubCategoryRepository.save(subCategoryEntity);
//        });
//
//        // Save Tech Stacks
//        lecturePackageDto.getTechStacks().forEach(techStackDto -> {
//            PackageTechStackEntity techStackEntity = new PackageTechStackEntity();
//            techStackEntity.setId(new PackageTechStackId(lecturePackageEntity.getId(), techStackDto.getId().getTechStackId()));
//            techStackEntity.setLecturePackage(lecturePackageEntity);
//            techStackEntity.setTechStack(techStackDto.getTechStack().toEntity());
//            packageTechStackRepository.save(techStackEntity);
//        });
//
//        // Convert Entity back to DTO using the method in DTO
//        return LecturePackageDto.fromEntity(lecturePackageEntity);
//    }
//}