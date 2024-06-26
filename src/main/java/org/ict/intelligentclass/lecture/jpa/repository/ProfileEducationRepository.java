package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.education.jpa.entity.EducationEntity;
import org.ict.intelligentclass.lecture.jpa.entity.output.UserProfileEduDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileEducationRepository extends JpaRepository<EducationEntity, Long> {
    List<EducationEntity> findByNickname(String nickname);
}
