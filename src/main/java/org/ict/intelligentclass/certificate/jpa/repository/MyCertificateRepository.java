package org.ict.intelligentclass.certificate.jpa.repository;


import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MyCertificateRepository extends JpaRepository<MyCertificateEntity, String> {

    List<MyCertificateEntity> findByNicknameContaining(String nickname);

}
