package org.ict.intelligentclass.lecture.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.input.CommentInput;
import org.ict.intelligentclass.lecture.jpa.entity.input.LectureInput;
import org.ict.intelligentclass.lecture.jpa.entity.input.LectureReadInput;
import org.ict.intelligentclass.lecture.jpa.entity.input.RatingInput;
import org.ict.intelligentclass.lecture.jpa.entity.output.*;
import org.ict.intelligentclass.lecture.model.dto.LectureCommentDto;
import org.ict.intelligentclass.lecture.model.service.LectureService;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LectureController {

    @Value("${lecture.github.token}")
    private String lectureGitHubToken;

    private final LectureService lectureService;

    // 강의 패키지 타이틀 가져오기
    @GetMapping("/title/{lecturePackageId}")
    public ResponseEntity<LecturePackageEntity> selectLecturePackageTitle(@PathVariable Long lecturePackageId) {
        return new ResponseEntity<>(lectureService.selectLecturePackageTitle(lecturePackageId), HttpStatus.OK);
    }


    // 강의 패키지 소유자 정보
    @GetMapping("/owner/{lecturePackageId}")
    public ResponseEntity<LectureOwnerDto> getLecturePackageOwner(@PathVariable Long lecturePackageId) {
        try {
            LectureOwnerDto ownerDto = lectureService.getLecturePackageOwner(lecturePackageId);
            return ResponseEntity.ok(ownerDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 강의 목록 페이지
    @GetMapping("/list/{lecturePackageId}")
    public List<LectureListDto> getLectureList(@PathVariable Long lecturePackageId) {
        return lectureService.getLectureList(lecturePackageId);
    }

    // 강의 패키지 별점 가져오기
    @GetMapping("/rating/{lecturePackageId}")
    public ResponseEntity<PackageRatingDto> selectLecturePackageRating(@PathVariable Long lecturePackageId) {
        PackageRatingDto ratingDto = lectureService.selectLecturePackageRating(lecturePackageId);
        return new ResponseEntity<>(ratingDto, HttpStatus.OK);
    }

    // 강의 패키지 별점 입력
    @PostMapping("/rating/{lecturePackageId}")
    public ResponseEntity<String> addRating(@PathVariable Long lecturePackageId, @RequestBody RatingInput ratingInput) {
        try {
            ratingInput.setLecturePackageId(lecturePackageId);
            lectureService.addRating(ratingInput);
            return ResponseEntity.status(HttpStatus.CREATED).body("별점이 등록 되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("별점을 이미 등록 하셨습니다.");
        }
    }

    // 강의 읽음 가져오기
    @GetMapping("/read-status/{lectureId}")
    public ResponseEntity<LectureReadStatusDto> getLectureReadStatus(
            @PathVariable("lectureId") int lectureId,
            @RequestParam("nickname") String nickname) {
        LectureReadStatusDto lectureReadStatus = lectureService.getLectureReadStatus(lectureId, nickname);
        return new ResponseEntity<>(lectureReadStatus, HttpStatus.OK);
    }

    // 강의 읽음 처리 저장
    @PostMapping("/update-read-status/{lectureId}")
    public ResponseEntity<String> updateReadStatus(@PathVariable("lectureId") int lectureId, @RequestBody LectureReadInput lectureReadInput) {
        lectureReadInput.setLectureId(lectureId);
        lectureService.updateLectureReadStatus(lectureReadInput);
        return ResponseEntity.ok("Lecture read status updated successfully");
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

    // 강의 조회수 증가
    @PostMapping("/increase-viewcount/{lectureId}")
    public ResponseEntity<Void> increaseViewCount(@PathVariable int lectureId) {
        lectureService.increaseViewCount(lectureId);
        return ResponseEntity.ok().build();
    }

    // 강의 추가
    @PostMapping("/register/{lecturePackageId}")
    public ResponseEntity<String> registerLecture(@PathVariable Long lecturePackageId, @RequestParam String nickname, @RequestBody LectureInput lectureInput) {
        try {
            lectureInput.setLecturePackageId(lecturePackageId); // lecturePackageId 설정
            lectureInput.setNickname(nickname); // nickname 설정
            if (lectureInput.getStreamUrl() == null || lectureInput.getStreamUrl().isEmpty()) {
                lectureInput.setStreamUrl("http://example.com/default-stream");
            }
            lectureService.registerLecture(lectureInput);
            return ResponseEntity.ok("Lecture registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering lecture");
        }
    }

    // 강의 삭제
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteLectures(@RequestBody Map<String, List<Integer>> request) {
        try {
            List<Integer> lectureIds = request.get("lectureIds");
            List<String> filePaths = lectureService.getFilePathsForLectures(lectureIds);

            // GitHub 파일 삭제
            for (String filePath : filePaths) {
                deleteFileFromGitHub(filePath);
            }

            // Oracle DB에서 강의 삭제
            lectureService.deleteLectures(lectureIds);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void deleteFileFromGitHub(String fileUrl) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "token " + lectureGitHubToken);
            headers.set("Accept", "application/vnd.github.v3+json");

            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            // GitHub API에서 파일 경로를 추출
            String filePath = extractFilePath(fileUrl);
            String url = "https://api.github.com/repos/rudalsdl/lectureSave/contents/" + filePath;

            // SHA 값을 가져오는 요청
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
            String sha = (String) response.getBody().get("sha");

            // 파일 삭제 요청
            Map<String, String> deleteBody = Map.of(
                    "message", "Delete file " + filePath,
                    "sha", sha
            );

            HttpEntity<Map<String, String>> deleteRequestEntity = new HttpEntity<>(deleteBody, headers);
            restTemplate.exchange(url, HttpMethod.DELETE, deleteRequestEntity, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String extractFilePath(String fileUrl) {
        // "https://raw.githubusercontent.com/rudalsdl/lectureSave/main/" 제거
        return fileUrl.replace("https://raw.githubusercontent.com/rudalsdl/lectureSave/main/", "");
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
