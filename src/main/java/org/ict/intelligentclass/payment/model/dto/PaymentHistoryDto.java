package org.ict.intelligentclass.payment.model.dto;

import lombok.*;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.ict.intelligentclass.lecture.jpa.entity.QLectureEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.payment.jpa.entity.PaymentEntity;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistoryDto {
    private Long transactionId;
    private String userEmail;
    private String provider;
    private Long lecturePackageId;
    private String paymentType;
    private Long couponId;
    private Double finalPrice;
    private LocalDateTime transactionDate;
    private String orderId;
    private String paymentConfirmation;
    private String title;
    private String thumbnail;
    private String paymentKey;
    private Integer lectureRead; // 새로운 필드

    public PaymentEntity toEntity(PaymentEntity entity, LecturePackageEntity lecturePackage) {
        entity.setTransactionId(this.transactionId);
        entity.setUserEmail(this.userEmail);
        entity.setProvider(this.provider);
        entity.setLecturePackageId(this.lecturePackageId);
        entity.setPaymentType(this.paymentType);
        entity.setCouponId(this.couponId);
        entity.setFinalPrice(this.finalPrice);
        entity.setTransactionDate(this.transactionDate);
        entity.setOrderId(this.orderId);
        entity.setPaymentConfirmation(this.paymentConfirmation);
        entity.setPaymentKey(this.paymentKey);
        return entity;
    }


}
