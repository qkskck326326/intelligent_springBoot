package org.ict.intelligentclass.payment.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "TB_COUPON")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CouponEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_COUPON_ID")
    @SequenceGenerator(name = "SQ_COUPON_ID", sequenceName = "SQ_COUPON_ID", allocationSize = 1)
    @Column(name = "COUPON_ID")
    private Long id;

    @Column(name = "USEREMAIL", nullable = false)
    private String userEmail;

    @Column(name = "PROVIDER", nullable = false)
    private String provider;

    @Column(name = "COUPON_DESCRIPTION")
    private String couponDescription;

    @Column(name = "DISCOUNT_AMOUNT", nullable = false)
    private Double discountAmount;

    @Column(name = "ISSUED_DATE")
    private LocalDate issuedDate;
}
