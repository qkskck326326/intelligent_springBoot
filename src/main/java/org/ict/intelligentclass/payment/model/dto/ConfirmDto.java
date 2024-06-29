package org.ict.intelligentclass.payment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfirmDto {
    private String userEmail;
    private String provider;
    private Long lecturePackageId;
    private String paymentConfirmation;
}
