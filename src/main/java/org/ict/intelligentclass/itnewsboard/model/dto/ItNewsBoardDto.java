package org.ict.intelligentclass.itnewsboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.itnewsboard.jpa.entity.ItNewsBoardEntity;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItNewsBoardDto {
    private Long boardId; // BOARD_ID 필드 추가
    private String siteUrl;
    private String boardUrl;
    private String title;
    private String videoTextlizedContext;
    private String originalContext;
    private Date registDate;

    public ItNewsBoardEntity toEntity() {
        return ItNewsBoardEntity.builder()
                .boardId(this.boardId)
                .siteUrl(this.siteUrl)
                .boardUrl(this.boardUrl)
                .title(this.title)
                .videoTextlizedContext(this.videoTextlizedContext)
                .originalContext(this.originalContext)
                .registDate(this.registDate)
                .build();
    }
}//
