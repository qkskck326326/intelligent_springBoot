package org.ict.intelligentclass.announcement.jpa.repository;

import org.ict.intelligentclass.announcement.jpa.entity.AnnouncementEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Integer> {

    Page<AnnouncementEntity> findByCategory(Long category, Pageable pageable);

    @Query("SELECT a FROM AnnouncementEntity a WHERE a.title LIKE %:keyword% OR a.content LIKE %:keyword%")
    Page<AnnouncementEntity> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
