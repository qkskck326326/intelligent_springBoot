package org.ict.intelligentclass.payment.jpa.repository;

import org.ict.intelligentclass.payment.jpa.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    @Query("SELECT p.couponId FROM PaymentEntity p WHERE p.userEmail = :userEmail AND p.couponId IS NOT NULL")
    List<Long> findUsedCouponIdsByUserEmail(@Param("userEmail") String userEmail);

    @Query("SELECT p.lecturePackageId FROM PaymentEntity p WHERE p.userEmail = :userEmail AND p.paymentConfirmation = 'Y'")
    List<Long> findPurchasedPackageIdsByUserEmail(@Param("userEmail") String userEmail);

    List<PaymentEntity> findByUserEmail(String userEmail);

    @Query("SELECT p FROM PaymentEntity p WHERE p.userEmail = :userEmail and p.provider = :provider " +
            "and p.lecturePackageId = :lecturePackageId and p.paymentConfirmation = :paymentConfirmation")
    Optional<PaymentEntity> getConfirmation(@Param("userEmail") String userEmail,
                                            @Param("provider") String provider,
                                            @Param("lecturePackageId") Long lecturePackageId,
                                            @Param("paymentConfirmation") String paymentConfirmation);
    // 태석 추가
    @Query("SELECT p.lecturePackageId FROM PaymentEntity p WHERE p.userEmail = :userEmail AND p.provider = :provider AND p.paymentConfirmation = 'Y'")
    List<Long> findLecturePackageIdsByUserEmailAndProvider(@Param("userEmail") String userEmail, @Param("provider") String provider);

    @Query("SELECT p, l.lectureRead FROM PaymentEntity p " +
            "LEFT JOIN LectureReadEntity l ON p.lecturePackageId = l.lectureId " +
            "WHERE p.userEmail = :userEmail")
    List<Object[]> findByUserEmailWithLectureRead(@Param("userEmail") String userEmail);
}
