package org.ict.intelligentclass.itnewssite.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.itnewssite.model.dto.ItNewsSiteDto;

import java.util.Date;


@Entity
@Table(name = "TB_IT_NEWS_SITE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItNewsSiteEntity {
    @Id
    @Column(name = "SITE_URL", length = 300)
    private String siteUrl;

    @Column(name = "LATEST_BOARD_URL", length = 300)
    private String latestBoardUrl;

    @Column(name = "SITE_NAME", length = 100)
    private String siteName;

    @Column(name = "TITLE_ELEMENT", length = 300)
    private String titleElement;

    @Column(name = "CONTEXT_ELEMENT", length = 300)
    private String contextElement;

    public ItNewsSiteDto toDto() {
        return ItNewsSiteDto.builder()
                .siteUrl(this.siteUrl)
                .latestBoardUrl(this.latestBoardUrl)
                .siteName(this.siteName)
                .titleElement(this.titleElement)
                .contextElement(this.contextElement)
                .build();
    }
}//
