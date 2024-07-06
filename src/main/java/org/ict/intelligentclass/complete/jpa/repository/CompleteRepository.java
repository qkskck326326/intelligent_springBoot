package org.ict.intelligentclass.complete.jpa.repository;

import org.ict.intelligentclass.complete.jpa.entity.CompleteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CompleteRepository extends JpaRepository<CompleteEntity, Long>{

    @Query("SELECT c FROM CompleteEntity c WHERE c.nickname = :nickname")
    List<CompleteEntity> findByNickname(@Param("nickname") String nickname);
}

