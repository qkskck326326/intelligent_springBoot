package org.ict.intelligentclass.career.jpa.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_CAREER")
public class CareerEntity {

    @Id
    @SequenceGenerator(name = "SQ_CAREER_ID" , sequenceName = "SQ_CAREER_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_CAREER_ID")
    @Column(name = "CAREER_ID")
    private Long careerId;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "INSTITUTION_NAME", nullable = false)
    private String institutionName;

    @Column(name = "DEPARTMENT", nullable = false)
    private String department;

    @Column(name = "POSITION", nullable = false)
    private String position;

    @Column(name = "START_DATE", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private Date endDate;

    @Column(name = "RESPONSIBILITIES")
    private String responsibilities;

}
