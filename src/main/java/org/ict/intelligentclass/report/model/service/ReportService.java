package org.ict.intelligentclass.report.model.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.report.jpa.entity.ReportEntity;
import org.ict.intelligentclass.report.jpa.repository.ReportRepository;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.ict.intelligentclass.user.jpa.entity.QUserEntity.userEntity;

@Slf4j
@AllArgsConstructor
@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;


    public List<ReportEntity> getReportListAll() {
        return reportRepository.findAllByOrderByReportDateDesc();
    }



    public ReportEntity insertReport(ReportEntity reportEntity){
        if(reportEntity.getReportDate() == null){
            reportEntity.setReportDate(new Date());
        }
        return reportRepository.save(reportEntity);
    }


    // 승인 시 피신고인 userEntity의 reportCount 증가시킴.

    public UserEntity incrementReportCount(String nickname, Long reportId) {
        //reportCount +1함.
        Optional<UserEntity> user = userRepository.findByNickname(nickname);
        if (!user.isPresent()) {
            throw new IllegalArgumentException("User with nickname " + nickname + " not found");
        }
        UserEntity userEntity = user.get();
        userEntity.setReportCount(userEntity.getReportCount() + 1);
        UserEntity reportCountUpdate = userRepository.save(userEntity);

        //승인처리완료한 report 삭제
        reportRepository.deleteById(reportId);
        return reportCountUpdate;
    }

    public void deleteReport(Long reportId) {
        reportRepository.deleteById(reportId);

    }


}
