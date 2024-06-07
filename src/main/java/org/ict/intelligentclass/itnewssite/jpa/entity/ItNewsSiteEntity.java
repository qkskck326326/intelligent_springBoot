package org.ict.intelligentclass.itnewssite.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.itnewssite.model.dto.ItNewsSiteDto;


@Entity
@Table(name = "TB_IT_NEWS_SITE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItNewsSiteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(name = "site_url", length = 300)
    private String siteUrl;

    @Column(name = "latest_board_url", length = 300)
    private String latestBoardUrl;

    @Column(name = "site_name", length = 100)
    private String siteName;

    @Column(name = "video_element", length = 500)
    private String videoElement;

    @Column(name = "title_element", length = 300)
    private String titleElement;

    @Column(name = "context_element", length = 300)
    private String contextElement;

    public ItNewsSiteDto toDto() {
        return ItNewsSiteDto.builder()
                .boardId(this.boardId)
                .siteUrl(this.siteUrl)
                .latestBoardUrl(this.latestBoardUrl)
                .siteName(this.siteName)
                .videoElement(this.videoElement)
                .titleElement(this.titleElement)
                .contextElement(this.contextElement)
                .build();
    }

}//
