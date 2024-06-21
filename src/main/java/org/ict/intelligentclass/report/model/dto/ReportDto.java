package org.ict.intelligentclass.report.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {

    private Long reportId;
    private String receiveNickname;
    private String doNickname;
    private String content;
    private Date reportDate;
    private Integer reportType;
    private Long contentId;
}
