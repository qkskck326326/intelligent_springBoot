package org.ict.intelligentclass.report.model.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;
import org.ict.intelligentclass.report.jpa.entity.ReportEntity;
import org.ict.intelligentclass.report.jpa.repository.ReportRepository;
import org.ict.intelligentclass.report.model.dto.ReportDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;



    public List<ReportDto> getReportListAll() {
        List<ReportEntity> reportEntities = reportRepository.findAll();
        return reportEntities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReportDto convertToDto(ReportEntity reportEntity) {
        return ReportDto.builder()
                .reportId(reportEntity.getReportId())
                .receiveNickname(reportEntity.getReceiveNickname())
                .doNickname(reportEntity.getDoNickname())
                .content(reportEntity.getContent())
                .reportDate(reportEntity.getReportDate())
                .reportType(reportEntity.getReportType())
                .contentId(reportEntity.getContentId())
                .build();
    }
}
