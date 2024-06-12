package org.ict.intelligentclass.lecture_packages.jpa.repository;


import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategoryEntity, Long> {
        List<SubCategoryEntity> findByUpperCategoryId(Long upperCategoryId);
    }



