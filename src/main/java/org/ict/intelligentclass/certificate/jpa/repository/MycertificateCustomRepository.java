//package org.ict.intelligentclass.certificate.jpa.repository;
//
//import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.Query;
//
//public interface MycertificateCustomRepository {
//
//    @Query(value = "select * from certificate where nickname order by registDate desc", nativeQuery = true)
//    Page<MyCertificateEntity> findByNickname(String nickname, Pageable pageable);
//}
