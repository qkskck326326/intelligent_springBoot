//package org.ict.intelligentclass.lecture_packages.controller;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//
//@Slf4j
//@RequiredArgsConstructor
//@CrossOrigin
//@RestController
//@RequestMapping("/api")
//public class FileUploadController {
//
//    @Value("${file.upload-dir}")
//    private String uploadDir;
//
//    @PostMapping("/upload")
//    public ResponseEntity<?> handleFileUpload(@RequestParam("upload") MultipartFile file) {
//        if (file.isEmpty()) {
//            return new ResponseEntity<>("Please select a file!", HttpStatus.OK);
//        }
//
//        try {
//            // 파일 저장 경로 생성
//            byte[] bytes = file.getBytes();
//            Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
//            Files.write(path, bytes);
//
//            // 업로드된 파일의 URL 반환
//            String fileUrl = "/uploads/" + file.getOriginalFilename();
//            return ResponseEntity.ok().body(new UploadResponse(true, fileUrl));
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    // 업로드된 파일의 URL 반환을 위한 클래스
//    public static class UploadResponse {
//        private boolean uploaded;
//        private String url;
//
//        public UploadResponse(boolean uploaded, String url) {
//            this.uploaded = uploaded;
//            this.url = url;
//        }
//
//        public boolean isUploaded() {
//            return uploaded;
//        }
//
//        public void setUploaded(boolean uploaded) {
//            this.uploaded = uploaded;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//
//        public void setUrl(String url) {
//            this.url = url;
//        }
//    }
//}