package org.ict.intelligentclass.lecture_packages.jpa.repository;


import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageTechStackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageTechStackRepository extends JpaRepository<PackageTechStackEntity, Long> {
}