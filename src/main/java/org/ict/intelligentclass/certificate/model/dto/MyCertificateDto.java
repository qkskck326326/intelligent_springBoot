package org.ict.intelligentclass.certificate.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;


import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyCertificateDto {

    private String certificateNumber;
    private String nickname;
    private String pdfFile;
    private String kind;
    private Date passDate;
    private String issuePlace;




    public MyCertificateEntity toEntity() {
        return new MyCertificateEntity(
                this.certificateNumber,
                this.nickname,
                this.pdfFile,
                this.kind,
                this.passDate,
                this.issuePlace
        );
    }

    public static MyCertificateDto fromEntity(MyCertificateEntity entity) {
        return new MyCertificateDto(
                entity.getCertificateNumber(),
                entity.getNickname(),
                entity.getPdfFile(),
                entity.getKind(),
                entity.getPassDate(),
                entity.getIssuePlace()
        );
    }
}
