package org.ict.intelligentclass.report.jpa.repository;


import org.ict.intelligentclass.report.jpa.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
    List<ReportEntity> findAllByOrderByReportDateDesc();

}
