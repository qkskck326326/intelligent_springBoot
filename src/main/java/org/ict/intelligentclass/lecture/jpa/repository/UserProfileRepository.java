package org.ict.intelligentclass.lecture.jpa.repository;

import org.ict.intelligentclass.career.jpa.entity.CareerEntity;
import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
import org.ict.intelligentclass.education.jpa.entity.EducationEntity;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.ict.intelligentclass.lecture.jpa.entity.output.UserProfileCareerDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.UserProfileCertifiDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.UserProfileEduDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserProfileRepository
    extends JpaRepository<LectureEntity, Integer> {

    List<LectureEntity> findByLecturePackageIdOrderByLectureIdAsc(Long lecturePackageId);
}
