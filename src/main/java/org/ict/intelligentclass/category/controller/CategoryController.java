package org.ict.intelligentclass.category.controller;


import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.category.service.CategoryService;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.UpperCategoryEntity;
import org.ict.intelligentclass.lecture_packages.jpa.output.SubCategoryAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    //상위카테고리 추가
    @PostMapping("/insertUpper")
    public ResponseEntity<UpperCategoryEntity> insertUpperCategory(@RequestBody UpperCategoryEntity upperCategory) {
        UpperCategoryEntity upperCategory1 = categoryService.insertUpperCategory(upperCategory);
        return ResponseEntity.ok(upperCategory1);
    }

    // 하위 카테고리 추가
    @PostMapping("/insertSub")
    public ResponseEntity<SubCategoryEntity> insertSubCategory(@RequestBody SubCategoryEntity subCategory) {
        SubCategoryEntity subCategory1 = categoryService.insertSubCategory(subCategory);
        return ResponseEntity.ok(subCategory1);
    }

    @DeleteMapping("/deleteUpper")
    public ResponseEntity<Void> deleteUpperCategory(@RequestParam Long upperCategoryId) {
        categoryService.deleteUpperCategory(upperCategoryId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteSub")
    public ResponseEntity<Void> deleteSubCategory(@RequestParam Long subCategoryId) {
        categoryService.deleteSubCategory(subCategoryId);
        return ResponseEntity.ok().build();
    }




//    @GetMapping("/suball")
//    public ResponseEntity<List<SubCategoryAll>> getAllSubCategory(){
//        List<SubCategoryAll> subCategoryAll = categoryService.getAllSubCategory();
//        return ResponseEntity.ok(subCategoryAll);
//    }
}
