package org.ict.intelligentclass.category.service;

import org.ict.intelligentclass.category.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.category.jpa.entity.UpperCategoryEntity;
import org.ict.intelligentclass.category.jpa.repository.SubCategoryRepository;
import org.ict.intelligentclass.category.jpa.repository.UpperCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
