package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileCertifiDto {

    private String nickname;
    private String kind;

    public UserProfileCertifiDto(MyCertificateEntity myCertificateEntity) {
        this.nickname = myCertificateEntity.getNickname();
        this.kind = myCertificateEntity.getKind();
    }
}
