package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyLecturePackageRepository
    extends JpaRepository<LecturePackageEntity, Long> {

    Page<LecturePackageEntity> findByNickname(String nickname, Pageable pageable);
}



