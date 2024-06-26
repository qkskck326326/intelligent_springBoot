package org.ict.intelligentclass.admin.jpa.repository;

import org.ict.intelligentclass.admin.jpa.entity.VisitCountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VisitCountRepository extends JpaRepository<VisitCountEntity, Long> {
    VisitCountEntity findByDateAndUserEmail(LocalDate date, String userEmail);

    List<VisitCountEntity> findAllByDateBetween(LocalDate startDate, LocalDate endDate);

    // 특정 날짜의 고유한 방문자 수를 조회하는 쿼리
    @Query("SELECT COUNT(DISTINCT v.userEmail) FROM VisitCountEntity v WHERE v.date = :date")
    Long countDistinctVisitorsByDate(LocalDate date);

    // 특정 기간의 고유한 방문자 수를 조회하는 쿼리
    @Query("SELECT v.date, COUNT(DISTINCT v.userEmail) FROM VisitCountEntity v WHERE v.date BETWEEN :startDate AND :endDate GROUP BY v.date ORDER BY v.date")
    List<Object[]> countDistinctVisitorsByDateRange(LocalDate startDate, LocalDate endDate);

}
