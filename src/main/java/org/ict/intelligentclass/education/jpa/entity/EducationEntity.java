package org.ict.intelligentclass.education.jpa.entity;

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
@Table(name = "TB_EDUCATION")
public class EducationEntity {





    @Id
    @SequenceGenerator(name = "SQ_EDUCATION_ID" , sequenceName = "SQ_EDUCATION_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_EDUCATION_ID")
    @Column(name = "EDUCATION_ID")
    private Long educationId;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "EDUCATION_LEVEL", nullable = false)
    private String educationLevel;

    @Column(name = "SCHOOL_NAME")
    private String schoolName;

    @Column(name = "MAJOR")
    private String major;

    @Column(name = "EDUCATION_STATUS")
    private String educationStatus;


    @Column(name = "ENTRY_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private Date entryDate;

    @Column(name = "GRADUATION_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private Date graduationDate;

    @Column(name = "HOME_AND_TRANSFER")
    private String homeAndTransfer;

    @Column(name = "PASS_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private Date passDate;

    @Column(name = "UNIVERSITY_LEVEL")
    private String universityLevel;

}