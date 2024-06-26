package org.ict.intelligentclass.payment.jpa.repository;

import org.ict.intelligentclass.payment.jpa.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface CouponRepositoy extends JpaRepository<CouponEntity, Long> {
    List<CouponEntity> findByUserEmail(String userEmail);

    List<CouponEntity> findByUserEmailAndProviderOrderByIssuedDateDesc(String userEmail, String provider);
}
