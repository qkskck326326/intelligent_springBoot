package org.ict.intelligentclass.category.service;


import jakarta.persistence.EntityNotFoundException;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.UpperCategoryEntity;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.jpa.output.SubCategoryAll;
import org.ict.intelligentclass.lecture_packages.jpa.repository.SubCategoryRepository;
import org.ict.intelligentclass.lecture_packages.jpa.repository.UpperCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private UpperCategoryRepository upperCategoryRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    public List<UpperCategoryEntity> getAllUpperCategories() {
        return upperCategoryRepository.findAll();
    }

    public List<SubCategoryEntity> getSubCategoriesByUpperCategoryId(Long upperCategoryId) {
        return subCategoryRepository.findByUpperCategoryId(upperCategoryId);
    }

    public List<SubCategoryEntity> getAllSubCategories() {
        return subCategoryRepository.findAll();
    }

    // 상위카테고리 추가
    public UpperCategoryEntity insertUpperCategory(UpperCategoryEntity upperCategory){
        UpperCategoryEntity upperCategoryEntity = upperCategoryRepository.save(upperCategory);
        return upperCategoryEntity;
    }

    public SubCategoryEntity insertSubCategory(SubCategoryEntity subCategory){
        // 상위 카테고리 ID가 있는지 확인하고 존재하는 상위 카테고리 객체를 가져옵니다.
        if (subCategory.getUpperCategory() == null || subCategory.getUpperCategory().getId() == null) {
            throw new IllegalArgumentException("상위 카테고리 ID가 필요합니다.");
        }

        Optional<UpperCategoryEntity> upperCategoryOptional = upperCategoryRepository.findById(subCategory.getUpperCategory().getId());
        if (!upperCategoryOptional.isPresent()) {
            throw new EntityNotFoundException("상위 카테고리를 찾을 수 없습니다.");
        }

        subCategory.setUpperCategory(upperCategoryOptional.get());

        SubCategoryEntity subCategoryEntity = subCategoryRepository.save(subCategory);
        return subCategoryEntity;
    }



    public void deleteUpperCategory(Long upperCategoryId) {
        upperCategoryRepository.deleteById(upperCategoryId);
    }

    public void deleteSubCategory(Long subCategoryId) {
        subCategoryRepository.deleteById(subCategoryId);
    }


}
