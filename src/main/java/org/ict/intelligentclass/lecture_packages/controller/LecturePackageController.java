package org.ict.intelligentclass.lecture_packages.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;
import org.ict.intelligentclass.lecture_packages.model.service.LecturePackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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


//    @GetMapping
//    public ResponseEntity<List<LecturePackageDto>> getLecturePackages() {
//
//
//        List<LecturePackageDto> lecturePackages = lecturePackageService.getLecturePackages();
//        return new ResponseEntity<>(lecturePackages, HttpStatus.OK);
//    }
//
//
//    @PostMapping
//    public ResponseEntity<LecturePackageDto> createLecturePackage(@RequestBody LecturePackageDto lecturePackageDto) {
//        LecturePackageDto createdLecturePackage = lecturePackageService.createLecturePackage(lecturePackageDto);
//        return ResponseEntity.ok(createdLecturePackage);
//    }
}