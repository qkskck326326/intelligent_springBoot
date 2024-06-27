package org.ict.intelligentclass.report.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_REPORT")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_REPORT_ID")
    @SequenceGenerator(name = "SQ_REPORT_ID" , sequenceName = "SQ_REPORT_ID", allocationSize = 1)
    @Column(name = "REPORT_ID")
    private Long reportId;

    @Column(name = "RECEIVE_NICKNAME", nullable = false, length = 50)
    private String receiveNickname;

    @Column(name = "DO_NICKNAME", nullable = false, length = 50)
    private String doNickname;

    @Column(name = "CONTENT", nullable = false, length = 500)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REPORT_DATE", nullable = false)
    private Date reportDate;

    @Column(name = "REPORT_TYPE", nullable = false)
    private Integer reportType;

    @Column(name = "CONTENT_ID", nullable = false)
    private Long contentId;

}
