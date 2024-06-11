package org.ict.intelligentclass.certificate.jpa.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.certificate.model.dto.MyCertificateDto;

import java.util.Date;

@Entity
@Table(name = "TB_MY_CERTIFICATE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyCertificateEntity {
    @Id
    @Column(name = "MY_CERTIFICATE_NUMBER")
    private String certificateNumber;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "PDF_FILE", nullable = false)
    private String pdfFile;

    @Column(name = "KIND", nullable = false)
    private String kind;

    @Column(name = "PASSDATE", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date passDate;

    @Column(name = "ISSUE_PLACE", nullable = false)
    private String issuePlace;


    // toDto 메서드
    public MyCertificateDto toDto() {
        return new MyCertificateDto(
                this.certificateNumber,
                this.nickname,
                this.pdfFile,
                this.kind,
                this.passDate,
                this.issuePlace
        );
    }

}
