package org.ict.intelligentclass.itnewsboard.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.itnewsboard.model.dto.ItNewsBoardDto;

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
    private Long boardId;

    @Column(length = 300, nullable = false)
    private String siteUrl;

    @Column(length = 300, nullable = false)
    private String title;

    @Column(length = 3000)
    private String videoTextlizedContext;

    @Column(length = 3000)
    private String originalContext;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registDate;

    public ItNewsBoardDto toDto() {
        return ItNewsBoardDto.builder()
                .boardId(this.boardId)
                .siteUrl(this.siteUrl)
                .title(this.title)
                .videoTextlizedContext(this.videoTextlizedContext)
                .originalContext(this.originalContext)
                .registDate(this.registDate)
                .build();
    }

}//
