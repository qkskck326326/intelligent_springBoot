package org.ict.intelligentclass.admin.jpa.repository;

import org.ict.intelligentclass.admin.jpa.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BannerRepository extends JpaRepository<BannerEntity, Long> {
    Optional<BannerEntity> findTopByOrderByIdDesc();
}
