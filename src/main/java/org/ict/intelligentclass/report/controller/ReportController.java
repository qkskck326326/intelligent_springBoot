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

    @GetMapping
    public ResponseEntity<List<ReportEntity>> getReportListAll(){
        List<ReportEntity> reportList = reportService.getReportListAll();
        return ResponseEntity.ok(reportList);
    }

    @PostMapping
    public ResponseEntity<ReportEntity> postReport(@RequestBody ReportEntity reportEntity){
        ReportEntity report = reportService.insertReport();
        return ResponseEntity.ok(report);
    }




}