package org.ict.intelligentclass.itnewsboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItNewsBoardDto {
    private Long boardId; // BOARD_ID 필드 추가
    private String siteUrl;
    private String title;
    private String videoTextlizedContext;
    private String originalContext;
    private Date registDate;
}//
