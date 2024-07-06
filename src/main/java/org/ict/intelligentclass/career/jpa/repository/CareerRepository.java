package org.ict.intelligentclass.career.jpa.repository;

import org.ict.intelligentclass.career.jpa.entity.CareerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CareerRepository extends JpaRepository<CareerEntity, Long> {

    @Query("SELECT e FROM CareerEntity e WHERE e.nickname = :nickname ORDER BY e.startDate DESC")
    List<CareerEntity> findByNickname(@Param("nickname") String nickname);

}
