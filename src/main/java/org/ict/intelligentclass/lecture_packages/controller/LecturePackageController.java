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



    @GetMapping
    public ResponseEntity<List<LecturePackageList>> getLecturePackages() {
        List<LecturePackageList> lecturePackages = lecturePackageService.getAllLecturePackages();
        return ResponseEntity.ok(lecturePackages);
    }

    @GetMapping("/detail")
    public ResponseEntity<LecturePackageDetail> getLecturePackageById(@RequestParam Long lecturePackageId) {
        log.info("getLecturePackageById : ", lecturePackageId);
        LecturePackageDetail lecturePackageDetail = lecturePackageService.getLecturePackageDetail(lecturePackageId);
        return ResponseEntity.ok(lecturePackageDetail);
    }

    @PostMapping
    public ResponseEntity<LecturePackageEntity> createLecturePackage(@RequestBody LecturePackageRegister lecturePackageRegister) {
        log.info("createLecturePackage : ", lecturePackageRegister);

        LecturePackageEntity savedLecturePackage = lecturePackageService.createLecturePackage(lecturePackageRegister);
        return ResponseEntity.ok(savedLecturePackage);

    }

    @PutMapping
    public ResponseEntity<LecturePackageEntity> updateLecturePackage(@RequestParam Long lecturePackageId,
                                                                     @RequestBody LecturePackageRegister lecturePackageRegister) {
        LecturePackageEntity modifyLecturePackage = lecturePackageService.modifyLecturePackage(lecturePackageId, lecturePackageRegister);
        return ResponseEntity.ok(modifyLecturePackage);

    }


    @DeleteMapping("/{lecturePackageId}")
    public ResponseEntity<Void> deleteLecturePackage(@PathVariable Long lecturePackageId) {
        lecturePackageService.deleteLecturePackage(lecturePackageId);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/view/{lecturePackageId}")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long lecturePackageId) {
        lecturePackageService.incrementViewCount(lecturePackageId);
        return ResponseEntity.ok().build();
    }



//    @PostMapping
//    public ResponseEntity<LecturePackageRegister> createLecturePackage(@RequestBody LecturePackageRegister lecturePackage) {
//
//
//
//        lecturePackageRegister.getLecturePackageId()
//
//        LecturePackageRegister createdLecturePackage = lecturePackageService.createLecturePackage(lecturePackage);
//        return ResponseEntity.ok(createdLecturePackage);
//    }






}