package org.ict.intelligentclass.lecture.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureListDto;
import org.ict.intelligentclass.lecture.model.dto.LectureDto;
import org.ict.intelligentclass.lecture.model.service.LectureService;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDto;
import org.ict.intelligentclass.lecture_packages.model.service.LecturePackageService;
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

    // 강의 읽음 처리 가져오기
//    @GetMapping("/read")
//    public ResponseEntity<List<LectureDto>> selectLectureRead(@RequestParam String nickname, @RequestParam int lectureId) {
//        return new ResponseEntity<>(lectureService.selectLectureRead(nickname, lectureId), HttpStatus.OK);
//    }



    // 강의 미리보기
//    @GetMapping("/lecturePreviews")
//    public List<LecturePreviewDto> getLecturePreviews(@RequestParam int lectureId) {
//        return lectureService.getLecturePreviewsByLectureId(lectureId);
//    }

    // 강의 읽음 처리




}
