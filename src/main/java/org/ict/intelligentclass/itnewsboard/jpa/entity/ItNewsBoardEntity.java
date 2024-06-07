package org.ict.intelligentclass.itnewsboard.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.itnewsboard.model.dto.ItNewsBoardDto;
import org.ict.intelligentclass.itnewssite.model.dto.ItNewsSiteDto;


import java.util.Date;

@Entity
@Table(name = "TB_IT_NEWS_BOARD")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItNewsBoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long boardId;

    @Column(name = "SITE_URL", length = 300)
    private String siteUrl;

    @Column(name = "TITLE", length = 300)
    private String title;

    @Column(name = "VIDEO_TEXTLIZED_CONTEXT", length = 3000)
    private String videoTextlizedContext;

    @Column(name = "ORIGINAL_CONTEXT", length = 3000)
    private String originalContext;

    @Column(name = "REGIST_DATE")
    private Date registDate;

    public ItNewsBoardDto toDto() {
        return ItNewsBoardDto.builder()
                .siteUrl(this.siteUrl)
                .title(this.title)
                .videoTextlizedContext(this.videoTextlizedContext)
                .originalContext(this.originalContext)
                .registDate(this.registDate)
                .build();
    }
}//
