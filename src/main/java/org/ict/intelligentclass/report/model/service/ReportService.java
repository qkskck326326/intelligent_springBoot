package org.ict.intelligentclass.report.model.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.report.jpa.entity.ReportEntity;
import org.ict.intelligentclass.report.jpa.repository.ReportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;


    public List<ReportEntity> getReportListAll() {
        return reportRepository.findAll();
    }

    public ReportEntity insertReport(){
        return reportRepository.save(ReportEntity.builder().build());
    }


}
