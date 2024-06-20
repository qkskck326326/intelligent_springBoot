package org.ict.intelligentclass.payment.jpa.repository;

import org.ict.intelligentclass.payment.jpa.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepositoy extends JpaRepository<CouponEntity, Long> {
}
