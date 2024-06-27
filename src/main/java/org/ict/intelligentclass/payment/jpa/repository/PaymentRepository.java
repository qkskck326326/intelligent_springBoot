package org.ict.intelligentclass.payment.jpa.repository;

import org.ict.intelligentclass.payment.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    @Query("SELECT p.couponId FROM PaymentEntity p WHERE p.userEmail = :userEmail AND p.couponId IS NOT NULL")
    List<Long> findUsedCouponIdsByUserEmail(@Param("userEmail") String userEmail);

    @Query("SELECT p.lecturePackageId FROM PaymentEntity p WHERE p.userEmail = :userEmail AND p.paymentConfirmation = 'Y'")
    List<Long> findPurchasedPackageIdsByUserEmail(@Param("userEmail") String userEmail);

    List<PaymentEntity> findByUserEmail(String userEmail);
}
