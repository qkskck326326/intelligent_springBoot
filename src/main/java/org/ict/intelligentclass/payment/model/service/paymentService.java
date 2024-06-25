package org.ict.intelligentclass.payment.model.service;

import org.ict.intelligentclass.payment.jpa.entity.CouponEntity;
import org.ict.intelligentclass.payment.jpa.entity.PaymentEntity;
import org.ict.intelligentclass.payment.jpa.repository.CouponRepositoy;
import org.ict.intelligentclass.payment.jpa.repository.PaymentRepository;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class paymentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CouponRepositoy couponRepositoy;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<CouponEntity> getCouponsByUserEmail(String userEmail) {
//        userRepository.findByEmail(userEmail);
        CouponEntity couponEntity = new CouponEntity();
        return couponRepositoy.findByUserEmail(userEmail);

    }

    public void savePayment(PaymentEntity paymentEntity) {
        if (paymentEntity.getTransactionDate() == null) {
            paymentEntity.setTransactionDate(LocalDateTime.now());
        }
        if ("0".equals(paymentEntity.getLecturePackageKindPrice())) {
            LocalDateTime subscriptionEndDate = paymentEntity.getTransactionDate().plus(1, ChronoUnit.MONTHS);
            paymentEntity.setSubscriptionEndDate(subscriptionEndDate);
        }
        paymentRepository.save(paymentEntity);
    }
}
