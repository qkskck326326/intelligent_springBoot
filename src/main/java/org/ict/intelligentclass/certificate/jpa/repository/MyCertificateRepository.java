package org.ict.intelligentclass.certificate.jpa.repository;


import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;




@Repository
public interface MyCertificateRepository extends JpaRepository<MyCertificateEntity, String> {
    Page<MyCertificateEntity> findByNickname(String nickname,  Pageable pageable);

}