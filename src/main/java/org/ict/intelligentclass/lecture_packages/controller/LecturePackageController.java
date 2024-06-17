package org.ict.intelligentclass.lecture_packages.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.model.service.LectureService;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.QTechStackEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.TechStackEntity;
import org.ict.intelligentclass.lecture_packages.jpa.input.LecturePackageRegister;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;
import org.ict.intelligentclass.lecture_packages.jpa.output.SubCategoryAll;
import org.ict.intelligentclass.lecture_packages.model.service.LecturePackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/packages")
public class LecturePackageController {

    private final LecturePackageService lecturePackageService;



    //패키지리스트 조회
    @GetMapping
    public ResponseEntity<List<LecturePackageList>> getLecturePackages() {
        List<LecturePackageList> lecturePackages = lecturePackageService.getAllLecturePackages();
        return ResponseEntity.ok(lecturePackages);
    }

    //패키지 상세보기
    @GetMapping("/detail")
    public ResponseEntity<LecturePackageDetail> getLecturePackageById(@RequestParam Long lecturePackageId) {
        log.info("getLecturePackageById : ", lecturePackageId);
        LecturePackageDetail lecturePackageDetail = lecturePackageService.getLecturePackageDetail(lecturePackageId);
        return ResponseEntity.ok(lecturePackageDetail);
    }

    //패키지 등록하기
    @PostMapping
    public ResponseEntity<LecturePackageEntity> createLecturePackage(@RequestBody LecturePackageRegister lecturePackageRegister) {
        log.info("createLecturePackage : ", lecturePackageRegister);

        LecturePackageEntity savedLecturePackage = lecturePackageService.createLecturePackage(lecturePackageRegister);
        return ResponseEntity.ok(savedLecturePackage);

    }
    //패키지 수정하기
    @PutMapping
    public ResponseEntity<LecturePackageEntity> updateLecturePackage(@RequestParam Long lecturePackageId,
                                                                     @RequestBody LecturePackageRegister lecturePackageRegister) {
        LecturePackageEntity modifyLecturePackage = lecturePackageService.modifyLecturePackage(lecturePackageId, lecturePackageRegister);
        return ResponseEntity.ok(modifyLecturePackage);

    }

    //패키지 삭제하기
    @DeleteMapping("/{lecturePackageId}")
    public ResponseEntity<Void> deleteLecturePackage(@PathVariable Long lecturePackageId) {
        lecturePackageService.deleteLecturePackage(lecturePackageId);
        return ResponseEntity.noContent().build();
    }

    //패키지조회수 1 올리기
    @PutMapping("/view/{lecturePackageId}")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long lecturePackageId) {
        lecturePackageService.incrementViewCount(lecturePackageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/categorysort/{categoryId}")
    public ResponseEntity<List<LecturePackageList>> getCategorySortPackages(@PathVariable Long categoryId) {
        List<LecturePackageList> lecturePackageLists= lecturePackageService.getCategorySortedPackages(categoryId);
        return ResponseEntity.ok(lecturePackageLists);
    }


}