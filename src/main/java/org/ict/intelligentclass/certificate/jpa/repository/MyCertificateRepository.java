package org.ict.intelligentclass.certificate.jpa.repository;


import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




@Repository
public interface MyCertificateRepository extends JpaRepository<MyCertificateEntity, String> {
//    @Query(value = "select c.MY_CERTIFICATE_NUMBER, c.PDF_FILE, c.KIND, c.PASSDATE, c.ISSUE_PLACE from TB_MY_CERTIFICATE c where c.NICKNAME order by c.PASSDATE")
    Page<MyCertificateEntity> findByNickname(String nickname, Pageable pageable);



}
