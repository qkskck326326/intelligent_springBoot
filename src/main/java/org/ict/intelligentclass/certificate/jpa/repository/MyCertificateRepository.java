package org.ict.intelligentclass.certificate.jpa.repository;


import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MyCertificateRepository extends JpaRepository<MyCertificateEntity, String> {

    @Query("SELECT m FROM MyCertificateEntity m WHERE m.nickname = :nickname")
    List<MyCertificateEntity> findByNickname(@Param("nickname") String nickname);



}
