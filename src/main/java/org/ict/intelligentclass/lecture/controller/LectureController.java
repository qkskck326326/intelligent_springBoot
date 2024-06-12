package org.ict.intelligentclass.lecture.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.input.RatingInput;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureListDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.LecturePreviewDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.PackageRatingDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureDetailDto;
import org.ict.intelligentclass.lecture.model.service.LectureService;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
@CrossOrigin
public class LectureController {

    private final LectureService lectureService;

    // 강의 패키지 타이틀 가져오기
    @GetMapping("/title")
    public ResponseEntity<LecturePackageEntity> selectLecturePackageTitle(Long lecturePackageId) {
        return new ResponseEntity<>(lectureService.selectLecturePackageTitle(lecturePackageId), HttpStatus.OK);
    }

    // 강의 목록 페이지
//    @GetMapping()
//    public ResponseEntity<List<LectureDto>> selectAllLecture(Long lecturePackageId) {
//        return new ResponseEntity<>(lectureService.selectAllLecture(lecturePackageId), HttpStatus.OK);
//    }
    @GetMapping("/list")
    public ResponseEntity<List<LectureListDto>> getAllLectures() {
        List<LectureListDto> lectureListDtos = lectureService.selectAllLecture();
        return ResponseEntity.ok(lectureListDtos);
    }

    // 강의 패키지 별점 가져오기
    @GetMapping("/rating")
    public ResponseEntity<PackageRatingDto> selectLecturePackageRating(@RequestParam Long lecturePackageId) {
        PackageRatingDto ratingDto = lectureService.selectLecturePackageRating(lecturePackageId);
        return new ResponseEntity<>(ratingDto, HttpStatus.OK);
    }

    // 강의 패키지 별점 입력
    @PostMapping("/rating")
    public ResponseEntity<Void> addRating(@RequestBody RatingInput ratingInput) {
        lectureService.addRating(ratingInput);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    // 강의 미리보기
    @GetMapping("/preview/{lectureId}")
    public ResponseEntity<LecturePreviewDto> getLecturePreview(@PathVariable("lectureId") int lectureId) {
        LecturePreviewDto lecturePreview = lectureService.getLecturePreviewById(lectureId);
        return new ResponseEntity<>(lecturePreview, HttpStatus.OK);
    }

    // 강의 상세 정보
    @GetMapping("/detail/{lectureId}")
    public ResponseEntity<LectureDetailDto> getLectureDetail(@PathVariable("lectureId") int lectureId) {
        LectureDetailDto lectureDetail = lectureService.getLectureDetailById(lectureId);
        return new ResponseEntity<>(lectureDetail, HttpStatus.OK);
    }

}
