package org.ict.intelligentclass.payment.model.service;

import lombok.extern.slf4j.Slf4j;
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
import org.ict.intelligentclass.payment.model.dto.ConfirmDto;
import org.ict.intelligentclass.payment.model.dto.PaymentHistoryDto;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    public List<PaymentHistoryDto> getTransactionHistoryByUserEmail(String userEmail) {
        List<PaymentEntity> paymentEntities = paymentRepository.findByUserEmail(userEmail);
        log.info("결제내역가져오기 엔티티확인 {} : " + paymentEntities);
        List<PaymentHistoryDto> paymentHistoryDtos = new ArrayList<>();
        for (PaymentEntity paymentEntity : paymentEntities) {
            LecturePackageEntity lecturePackage = lecturePackageRepository.findById(paymentEntity.getLecturePackageId()).orElse(null);

            PaymentHistoryDto paymentHistoryDto = paymentEntity.toDto(new PaymentHistoryDto());
            String title = lecturePackage.getTitle();
            String thumbnail = lecturePackage.getThumbnail();
            paymentHistoryDto.setTitle(title);
            paymentHistoryDto.setThumbnail(thumbnail);
            paymentHistoryDtos.add(paymentHistoryDto);
            log.info("결제내역가져오기 확인 {}" + paymentHistoryDto);
        }

        return paymentHistoryDtos;
    }
    public List<ConfirmDto> getConfirmation() {
        List<ConfirmDto> confirmDtos = new ArrayList<>();
        List<PaymentEntity> paymentEntities = paymentRepository.getConfirmation();
        for (PaymentEntity paymentEntity : paymentEntities) {
            ConfirmDto confirmDto = new ConfirmDto();
            confirmDto.setUserEmail(paymentEntity.getUserEmail());
            confirmDto.setProvider(paymentEntity.getProvider());
            confirmDto.setLecturePackageId(paymentEntity.getLecturePackageId());
            confirmDto.setPaymentConfirmation(paymentEntity.getPaymentConfirmation());
            confirmDtos.add(confirmDto);
        }
        return confirmDtos;
    }
}
