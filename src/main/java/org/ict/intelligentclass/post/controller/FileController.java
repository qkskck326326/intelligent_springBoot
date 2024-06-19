package org.ict.intelligentclass.post.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.post.storage.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@NoArgsConstructor
@AllArgsConstructor
@RequestMapping("/api/files")
@CrossOrigin
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("upload") MultipartFile file) {
        try {
            // 파일 저장
            String fileName = fileStorageService.storeFile(file);
            String fileUrl = "http://localhost:8080/api/files/" + fileName; // 클라이언트가 접근할 수 있는 URL 경로

            // 업로드된 파일의 URL을 반환
            Map<String, String> response = new HashMap<>();
            response.put("url", fileUrl);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 오류 처리
            logger.error("Failed to upload file", e);
            return ResponseEntity.status(500).body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/{fileName:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        logger.info("Received request for file: {}", fileName);
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        String contentType = "application/octet-stream"; // 기본 값
        if (fileName.endsWith(".pdf")) {
            contentType = "application/pdf";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            contentType = "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            contentType = "image/png";
        } else if (fileName.endsWith(".mp4")) { // 동영상 파일 형식 추가
            contentType = "video/mp4";
        } else if (fileName.endsWith(".avi")) {
            contentType = "video/x-msvideo";
        }


        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(resource);

    }
}
