package org.ict.intelligentclass.payment.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

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
    private char paymentType;

    @Column(name = "COUPON_ID")
    private Long couponId;

    @Column(name = "FINAL_PRICE", nullable = false)
    private Double finalPrice;

    @Column(name = "TRANSACTION_DATE", nullable = false)
    private Timestamp transactionDate;

    @Column(name = "SUBSCRIPTION_END_DATE")
    private Timestamp subscriptionEndDate;

    @Column(name = "LECTURE_PACKAGE_KIND_PRICE", nullable = false)
    private char lecturePackageKindPrice;

    @Column(name = "PAYMENT_CONFIRMATION", nullable = false)
    private char paymentConfirmation;
}
