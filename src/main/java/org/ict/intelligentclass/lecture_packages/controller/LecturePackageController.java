package org.ict.intelligentclass.lecture_packages.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;

import org.ict.intelligentclass.lecture_packages.jpa.input.LecturePackageRegister;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;

import org.ict.intelligentclass.lecture_packages.model.service.LecturePackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


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

    // 카테고리로 필터링된 패키지 리스트 조회
    @GetMapping("/categorysort")
    public ResponseEntity<List<LecturePackageList>> getCategorySortPackages(@RequestParam Long categoryId) {
        List<LecturePackageList> lecturePackageLists = lecturePackageService.getCategorySortedPackages(categoryId);
        log.info("getCategorySortPackages : {}", lecturePackageLists);
        return ResponseEntity.ok(lecturePackageLists);
    }

    // 유저관심패키지 TOP10
    @GetMapping("/interestpackagetop10")
    public ResponseEntity<List<LecturePackageList>> getInterestPackages(@RequestParam String email, @RequestParam String provider) {
        List<LecturePackageList> lecturePackageLists = lecturePackageService.getInterestPackages(email, provider);

        log.info("email, provider : ", email, provider);
        return ResponseEntity.ok(lecturePackageLists);
    }

    // 모든 상위카테고리별 패키지 TOP4
    @GetMapping("/upperCategorypackageall")
    public ResponseEntity<Map<Long, List<LecturePackageList>>> getUpperCategoryPackageALl(){
        Map<Long, List<LecturePackageList>> lecturePackageLists = lecturePackageService.getUpperCategoryPackageAll();
        return ResponseEntity.ok(lecturePackageLists);
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








}