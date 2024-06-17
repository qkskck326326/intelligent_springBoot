package org.ict.intelligentclass.category.controller;


import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.category.service.CategoryService;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.UpperCategoryEntity;
import org.ict.intelligentclass.lecture_packages.jpa.output.SubCategoryAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/upper")
    public List<UpperCategoryEntity> getAllUpperCategories() {
        return categoryService.getAllUpperCategories();
    }

    @GetMapping("/sub/{upperCategoryId}")
    public List<SubCategoryEntity> getSubCategoriesByUpperCategoryId(@PathVariable Long upperCategoryId) {
        return categoryService.getSubCategoriesByUpperCategoryId(upperCategoryId);
    }

    @GetMapping("/sub")
    public List<SubCategoryEntity> getAllSubCategories() {
        log.info("getAllSubCategories : ", categoryService.getAllSubCategories());
        return categoryService.getAllSubCategories();
    }

//    @GetMapping("/suball")
//    public ResponseEntity<List<SubCategoryAll>> getAllSubCategory(){
//        List<SubCategoryAll> subCategoryAll = categoryService.getAllSubCategory();
//        return ResponseEntity.ok(subCategoryAll);
//    }
}
