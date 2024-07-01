package org.ict.intelligentclass.user.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.user.jpa.entity.AttendanceEntity;
import org.ict.intelligentclass.user.jpa.entity.id.AttendanceId;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceDto {
    private String userEmail;
    private String provider;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate attendanceTime;

    // dto -> entity method
    public AttendanceEntity toEntity() {
        AttendanceId attendanceId = new AttendanceId(this.userEmail, this.provider, this.attendanceTime);
        return AttendanceEntity.builder()
                .attendanceId(attendanceId)
                .build();
    }
}