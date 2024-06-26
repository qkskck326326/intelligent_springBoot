package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
import org.ict.intelligentclass.lecture.jpa.entity.output.UserProfileCertifiDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileCertifiRepository extends JpaRepository<MyCertificateEntity, Long> {
    List<MyCertificateEntity> findByNickname(String nickname);
}
