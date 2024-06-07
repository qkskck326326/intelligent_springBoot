package org.ict.intelligentclass.itnewsboard.jpa.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.ict.intelligentclass.itnewsboard.jpa.entity.ItNewsBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ItNewsBoardRepository extends JpaRepository<ItNewsBoardEntity, String> {

}//
