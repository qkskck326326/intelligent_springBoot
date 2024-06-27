package org.ict.intelligentclass.payment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private Long lecturePackageId;
    private String title;
    private String content;
    private int price;
    private String thumbnail;
    private String Nickname;
}
