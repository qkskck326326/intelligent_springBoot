package org.ict.intelligentclass.lecture_packages.jpa.repository;


import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturePackageRepository extends JpaRepository<LecturePackageEntity, Long> {


}