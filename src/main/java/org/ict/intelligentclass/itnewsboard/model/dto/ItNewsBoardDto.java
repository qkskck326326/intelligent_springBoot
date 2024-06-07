package org.ict.intelligentclass.itnewsboard.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class ItNewsBoardDto {
    private Long boardId;
    private String siteUrl;
    private String title;
    private String videoTextlizedContext;
    private String originalContext;
    private Date registDate;


}//
