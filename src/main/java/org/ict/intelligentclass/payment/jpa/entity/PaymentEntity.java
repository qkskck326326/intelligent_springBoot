package org.ict.intelligentclass.payment.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.payment.model.dto.PaymentHistoryDto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_TRANSACTION_HISTORY")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TRANSACTION_ID")
    @SequenceGenerator(name = "SQ_TRANSACTION_ID", sequenceName = "SQ_TRANSACTION_ID", allocationSize = 1)
    @Column(name = "TRANSACTION_ID")
    private Long transactionId;

    @Column(name = "USEREMAIL", nullable = false)
    private String userEmail;

    @Column(name = "PROVIDER", nullable = false)
    private String provider;

    @Column(name = "LECTURE_PACKAGE_ID", nullable = false)
    private Long lecturePackageId;

    @Column(name = "PAYMENT_TYPE", nullable = false)
    private String paymentType;

    @Column(name = "COUPON_ID")
    private Long couponId;

    @Column(name = "FINAL_PRICE", nullable = false)
    private Double finalPrice;

    @Column(name = "TRANSACTION_DATE", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "PAYMENTKEY")
    private String paymentKey;

    @Column(name = "PAYMENT_CONFIRMATION", nullable = false)
    private String paymentConfirmation;

    @Column(name = "ORDERID", nullable = false)
    private String orderId;

    public PaymentHistoryDto toDto(PaymentHistoryDto paymentHistoryDto) {
        paymentHistoryDto.setTransactionId(this.transactionId);
        paymentHistoryDto.setUserEmail(this.userEmail);
        paymentHistoryDto.setProvider(this.provider);
        paymentHistoryDto.setLecturePackageId(this.lecturePackageId);
        paymentHistoryDto.setPaymentType(this.paymentType);
        paymentHistoryDto.setCouponId(this.couponId);
        paymentHistoryDto.setFinalPrice(this.finalPrice);
        paymentHistoryDto.setTransactionDate(this.transactionDate);
        paymentHistoryDto.setOrderId(this.orderId);
        paymentHistoryDto.setPaymentConfirmation(this.paymentConfirmation);
        paymentHistoryDto.setPaymentKey(this.paymentKey);
        return paymentHistoryDto;
    }
}
