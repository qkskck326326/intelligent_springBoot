package org.ict.intelligentclass.user.jpa.repository;


import org.ict.intelligentclass.user.jpa.entity.AttendanceEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<AttendanceEntity, UserId> {
    @Query("SELECT a FROM AttendanceEntity a WHERE a.attendanceId.userEmail = :email AND a.attendanceId.provider = :provider")
    List<AttendanceEntity> selectAttendance(@Param("email") String email, @Param("provider") String provider);
}