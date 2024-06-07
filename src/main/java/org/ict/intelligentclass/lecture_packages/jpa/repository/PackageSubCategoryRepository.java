package org.ict.intelligentclass.lecture_packages.jpa.repository;


import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageSubCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageSubCategoryRepository extends JpaRepository<PackageSubCategoryEntity, Long> {
}
