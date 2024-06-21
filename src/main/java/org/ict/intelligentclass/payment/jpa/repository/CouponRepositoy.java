package org.ict.intelligentclass.payment.jpa.repository;

import org.ict.intelligentclass.payment.jpa.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRepositoy extends JpaRepository<CouponEntity, Long> {
    List<CouponEntity> findByUserEmail(String userEmail);
}
