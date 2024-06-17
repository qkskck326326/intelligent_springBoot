package org.ict.intelligentclass.lecture.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.input.CommentInput;
import org.ict.intelligentclass.lecture.jpa.entity.input.LectureInput;
import org.ict.intelligentclass.lecture.jpa.entity.input.RatingInput;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureListDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.LecturePreviewDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.PackageRatingDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureDetailDto;
import org.ict.intelligentclass.lecture.model.dto.LectureCommentDto;
import org.ict.intelligentclass.lecture.model.dto.LectureReadDto;
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
@CrossOrigin(origins = "http://localhost:3000")
public class LectureController {

    private final LectureService lectureService;

    // 강의 패키지 타이틀 가져오기
    @GetMapping("/title/{lecturePackageId}")
    public ResponseEntity<LecturePackageEntity> selectLecturePackageTitle(@PathVariable Long lecturePackageId) {
        return new ResponseEntity<>(lectureService.selectLecturePackageTitle(lecturePackageId), HttpStatus.OK);
    }

    // 강의 목록 페이지
    @GetMapping("/list/{lecturePackageId}")
    public ResponseEntity<List<LectureListDto>> getAllLectures(@PathVariable Long lecturePackageId) {
        List<LectureListDto> lectureListDtos = lectureService.selectAllLecture(lecturePackageId);
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

    // 강의 읽음 처리
    @PostMapping("/changeLectureRead")
    public ResponseEntity<String> changeLectureRead(@RequestBody LectureReadDto lectureReadDto) {
        lectureService.changeLectureRead(lectureReadDto.getLectureId(), lectureReadDto.getNickname());
        return ResponseEntity.ok("Lecture marked as read");
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

    // 강의 추가
    @PostMapping("/register")
    public ResponseEntity<String> registerLecture(@RequestBody LectureInput lectureInput, @RequestParam Long lecturePackageId, @RequestParam String nickname) {
        try {
            if (lectureInput.getStreamUrl() == null || lectureInput.getStreamUrl().isEmpty()) {
                lectureInput.setStreamUrl("http://example.com/default-stream");
            }
            lectureService.registerLecture(lectureInput, lecturePackageId, nickname);
            return ResponseEntity.ok("Lecture registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering lecture");
        }
    }

    // 강의 댓글 목록
    @GetMapping("/comments/{lectureId}")
    public ResponseEntity<List<LectureCommentDto>> getLectureComments(@PathVariable("lectureId") int lectureId) {
        List<LectureCommentDto> lectureComments = lectureService.getLectureComments(lectureId);
        return new ResponseEntity<>(lectureComments, HttpStatus.OK);
    }

    // 강의 댓글 추가
    @PostMapping("/comments/{lectureId}")
    public ResponseEntity<String> insertLectureComment(@RequestBody CommentInput commentInput) {
        lectureService.insertLectureComment(commentInput);
        return new ResponseEntity<>("Comment added successfully", HttpStatus.CREATED);
    }

    // 강의 댓글 수정
    @PutMapping("/comments/{lectureCommentId}")
    public ResponseEntity<String> updateLectureComment(@PathVariable("lectureCommentId") int lectureCommentId, @RequestBody String lectureCommentContent) {
        lectureService.updateLectureComment(lectureCommentId, lectureCommentContent);
        return new ResponseEntity<>("Comment updated successfully", HttpStatus.OK);
    }

    // 강의 댓글 삭제
    @DeleteMapping("/comments/{lectureCommentId}")
    public ResponseEntity<String> deleteLectureComment(@PathVariable("lectureCommentId") int lectureCommentId) {
        lectureService.deleteLectureComment(lectureCommentId);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }



}
