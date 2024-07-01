package org.ict.intelligentclass.user.jpa.repository;


import org.ict.intelligentclass.user.jpa.entity.AttendanceEntity;
import org.ict.intelligentclass.user.jpa.entity.id.AttendanceId;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, AttendanceId> {
    Optional<AttendanceEntity> findByAttendanceIdUserEmailAndAttendanceIdProviderAndAttendanceIdAttendanceTime(String userEmail, String provider, LocalDate attendanceTime);

    @Query("SELECT a FROM AttendanceEntity a WHERE a.attendanceId.userEmail = :email AND a.attendanceId.provider = :provider")
    List<AttendanceEntity> selectAttendance(@Param("email") String email, @Param("provider") String provider);

    // 특정 기간의 출석 기록을 조회하는 메서드
    List<AttendanceEntity> findByUser_UserId_UserEmailAndUser_UserId_ProviderAndAttendanceId_AttendanceTimeBetween(String userEmail, String provider, LocalDate startDate, LocalDate endDate);
}