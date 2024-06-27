package org.ict.intelligentclass.admin.model.service;

import lombok.RequiredArgsConstructor;
import org.ict.intelligentclass.admin.jpa.entity.VisitCountEntity;
import org.ict.intelligentclass.admin.jpa.repository.VisitCountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitCountService {
    private final VisitCountRepository visitCountRepository;

    @Transactional
    public void recordVisit(String userEmail) {
        LocalDate today = LocalDate.now();
        VisitCountEntity visitCount = new VisitCountEntity();
        visitCount.setDate(today);
        visitCount.setUserEmail(userEmail);
        visitCount.setCount(1L);

        visitCountRepository.save(visitCount);
    }

    public Long getDistinctVisitorCountByDate(LocalDate date) {
        return visitCountRepository.countDistinctVisitorsByDate(date);
    }

    public List<VisitCountEntity> getDistinctVisitorCountByDateRange(LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = visitCountRepository.countDistinctVisitorsByDateRange(startDate, endDate);
        return results.stream()
                .map(result -> {
                    VisitCountEntity entity = new VisitCountEntity();
                    entity.setDate((LocalDate) result[0]);
                    entity.setCount((Long) result[1]);
                    return entity;
                })
                .collect(Collectors.toList());
    }


}
