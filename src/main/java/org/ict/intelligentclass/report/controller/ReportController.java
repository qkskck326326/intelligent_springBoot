package org.ict.intelligentclass.report.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.report.jpa.entity.ReportEntity;
import org.ict.intelligentclass.report.model.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    //리스트 조회
    @GetMapping
    public ResponseEntity<List<ReportEntity>> getReportListAll(){
        List<ReportEntity> reportList = reportService.getReportListAll();
        return ResponseEntity.ok(reportList);
    }

    //신고클릭시 추가
    @PostMapping
    public ResponseEntity<ReportEntity> postReport(@RequestBody ReportEntity reportEntity){
        ReportEntity report = reportService.insertReport(reportEntity);
        return ResponseEntity.ok(report);
    }

    //승인 처리 -> user테이블에 reportCount 수정
    @PostMapping("/increment")
    public ResponseEntity<ReportEntity> incrementReportCount(@RequestParam String nickname, @RequestParam Long reportId) {
        reportService.incrementReportCount(nickname, reportId);
        return ResponseEntity.ok().build();
    }

    //삭제 처리
    @DeleteMapping
    public ResponseEntity<ReportEntity> deleteReport(@RequestParam Long reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.ok().build();
    }





}