package org.ict.intelligentclass.admin.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerDto {
    private int id;
    private String title;
    private String imageUrl;
    private String linkUrl;
}
