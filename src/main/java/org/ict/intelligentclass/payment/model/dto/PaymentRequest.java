package org.ict.intelligentclass.payment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String orderId;
    private Double amount;
    private String userEmail;
    private Long lecturePackageId;
    private String paymentMethod;
    private Long couponId;
    private String priceKind;
    private String provider;
}
