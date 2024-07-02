package org.ict.intelligentclass.itnewssite.jpa.repository;

import org.ict.intelligentclass.itnewsboard.jpa.entity.ItNewsBoardEntity;
import org.ict.intelligentclass.itnewssite.jpa.entity.ItNewsSiteEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItNewsSiteRepository extends JpaRepository<ItNewsSiteEntity, String> {

    List<ItNewsSiteEntity> findBySiteNameContaining(String siteName, Pageable pageable);
}//
