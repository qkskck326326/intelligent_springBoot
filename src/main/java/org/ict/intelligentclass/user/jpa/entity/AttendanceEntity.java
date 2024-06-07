package org.ict.intelligentclass.user.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.user.jpa.entity.id.AttendanceId;
import org.ict.intelligentclass.user.model.dto.AttendanceDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_ATTENDANCE")
public class AttendanceEntity {
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "attendanceId.userEmail", column = @Column(name = "USEREMAIL")),
            @AttributeOverride(name = "attendanceId.provider", column = @Column(name = "PROVIDER")),
            @AttributeOverride(name = "attendanceId.attendanceTime", column = @Column(name = "ATTENDANCE_TIME"))
    })
    private AttendanceId attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumns({
            @JoinColumn(name = "USEREMAIL", referencedColumnName = "USEREMAIL"),
            @JoinColumn(name = "PROVIDER", referencedColumnName = "PROVIDER")
    })
    private UserEntity user;

    public AttendanceDto toDto() {
        return AttendanceDto.builder()
                .userEmail(this.attendanceId.getUserEmail())
                .provider(this.attendanceId.getProvider())
                .attendanceTime(this.attendanceId.getAttendanceTime())
                .build();
    }
}
