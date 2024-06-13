package org.ict.intelligentclass.lecture_packages.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.input.LecturePackageRegister;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;
import org.ict.intelligentclass.lecture_packages.jpa.output.SubCategoryAll;
import org.ict.intelligentclass.lecture_packages.model.service.LecturePackageService;
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