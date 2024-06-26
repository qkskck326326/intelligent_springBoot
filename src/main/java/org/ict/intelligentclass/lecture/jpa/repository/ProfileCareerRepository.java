package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.career.jpa.entity.CareerEntity;
import org.ict.intelligentclass.lecture.jpa.entity.output.UserProfileCareerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileCareerRepository extends JpaRepository<CareerEntity, Long> {
    List<CareerEntity> findByNickname(String nickname);
}
