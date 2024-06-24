package org.ict.intelligentclass.admin.model.dto;

import lombok.Data;

@Data
public class BannerDto {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
}
