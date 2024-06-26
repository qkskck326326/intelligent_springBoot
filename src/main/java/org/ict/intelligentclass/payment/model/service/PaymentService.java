package org.ict.intelligentclass.payment.model.service;

import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.lecture_packages.jpa.repository.LecturePackageRepository;
import org.ict.intelligentclass.payment.jpa.entity.CartEntity;
import org.ict.intelligentclass.payment.jpa.entity.CartItemEntity;
import org.ict.intelligentclass.payment.jpa.entity.CouponEntity;
import org.ict.intelligentclass.payment.jpa.entity.PaymentEntity;
import org.ict.intelligentclass.payment.jpa.repository.CartItemRepository;
import org.ict.intelligentclass.payment.jpa.repository.CartRepository;
import org.ict.intelligentclass.payment.jpa.repository.CouponRepositoy;
import org.ict.intelligentclass.payment.jpa.repository.PaymentRepository;
import org.ict.intelligentclass.payment.model.dto.CartItemDto;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepositoy couponRepositoy;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private LecturePackageRepository lecturePackageRepository;

    @Autowired
    private CouponRepositoy couponyRepository;



    public List<CouponEntity> getCouponsByUserEmail(String userEmail) {
        List<CouponEntity> allCoupons = couponyRepository.findByUserEmail(userEmail);
        List<Long> usedCouponIds = paymentRepository.findUsedCouponIdsByUserEmail(userEmail);
        return allCoupons.stream()
                .filter(coupon -> !usedCouponIds.contains(coupon.getId()))
                .collect(Collectors.toList());
    }

    public void savePayment(PaymentEntity paymentEntity) {
        if (paymentEntity.getTransactionDate() == null) {
            paymentEntity.setTransactionDate(LocalDateTime.now());
        }
        paymentRepository.save(paymentEntity);
    }


    public void deleteCoupon(Long couponId) {
        paymentRepository.deleteById(couponId);
    }
}
