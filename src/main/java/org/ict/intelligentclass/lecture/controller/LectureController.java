package org.ict.intelligentclass.lecture.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.model.dto.LectureDto;
import org.ict.intelligentclass.lecture.model.service.LectureService;
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

    // 강의 목록 페이지
//    @GetMapping()
//    public ResponseEntity<List<LectureDto>> selectAllLecture(Long lecturePackageId) {
//        return new ResponseEntity<>(lectureService.selectAllLecture(lecturePackageId), HttpStatus.OK);
//    }
    @GetMapping("/list")
    public ResponseEntity<List<LectureDto>> selectAllLecture() {
        return new ResponseEntity<>(lectureService.selectAllLecture(), HttpStatus.OK);
    }


    // 강의 미리보기
//    @GetMapping("/lecturePreviews")
//    public List<LecturePreviewDto> getLecturePreviews(@RequestParam int lectureId) {
//        return lectureService.getLecturePreviewsByLectureId(lectureId);
//    }

    // 강의 읽음 처리




}
