package org.ict.intelligentclass.category.jpa.repository;

import org.ict.intelligentclass.category.jpa.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {
    List<SubCategoryEntity> findByUpperCategoryId(Long upperCategoryId);
}
