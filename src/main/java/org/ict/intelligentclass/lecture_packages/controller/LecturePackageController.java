package org.ict.intelligentclass.lecture_packages.controller;


import org.ict.intelligentclass.lecture_packages.model.dto.LecturePackageDto;
import org.ict.intelligentclass.lecture_packages.model.service.LecturePackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lecture-packages")
public class LecturePackageController {

    private final LecturePackageService lecturePackageService;

    public LecturePackageController(LecturePackageService lecturePackageService) {
        this.lecturePackageService = lecturePackageService;
    }

    @PostMapping
    public ResponseEntity<LecturePackageDto> createLecturePackage(@RequestBody LecturePackageDto lecturePackageDto) {
        LecturePackageDto createdLecturePackage = lecturePackageService.createLecturePackage(lecturePackageDto);
        return ResponseEntity.ok(createdLecturePackage);
    }
}