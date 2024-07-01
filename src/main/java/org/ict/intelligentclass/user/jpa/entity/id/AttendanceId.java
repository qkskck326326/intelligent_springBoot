package org.ict.intelligentclass.user.jpa.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Builder
public class AttendanceId implements Serializable {

    @Column(name = "USEREMAIL")
    private String userEmail;

    @Column(name = "PROVIDER")
    private String provider;

    @Column(name = "ATTENDANCE_TIME")
    private LocalDate attendanceTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttendanceId that = (AttendanceId) o;
        return attendanceTime == that.attendanceTime && Objects.equals(userEmail, that.userEmail) && Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, provider, attendanceTime);
    }
}
