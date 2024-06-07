package org.ict.intelligentclass.announcement.jpa.repository;

import org.ict.intelligentclass.announcement.jpa.entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Integer> {

}
