package org.ict.intelligentclass.education.jpa.repository;

import org.ict.intelligentclass.education.jpa.entity.EducationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EducationRepository extends JpaRepository<EducationEntity, Long> {


    @Query("SELECT e FROM EducationEntity e WHERE e.nickname = :nickname ORDER BY e.entryDate  DESC ")
    List<EducationEntity> findByNickname(@Param("nickname") String nickname);

}
