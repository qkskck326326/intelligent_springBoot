package org.ict.intelligentclass.itnewssite.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.itnewssite.jpa.entity.ItNewsSiteEntity;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class ItNewsSiteDto {
    private String siteUrl;
    private String latestBoardUrl;
    private String siteName;
    private String titleElement;
    private String contextElement;

    public ItNewsSiteEntity toEntity() {
        return ItNewsSiteEntity.builder()
                .siteUrl(this.siteUrl)
                .latestBoardUrl(this.latestBoardUrl)
                .siteName(this.siteName)
                .titleElement(this.titleElement)
                .contextElement(this.contextElement)
                .build();
    }
}//
