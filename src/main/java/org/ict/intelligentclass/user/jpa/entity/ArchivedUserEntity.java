package org.ict.intelligentclass.user.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.model.dto.ArchivedUserDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_ARCHIVED_USER")
public class ArchivedUserEntity {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "userId.userEmail", column = @Column(name = "USEREMAIL")),
            @AttributeOverride(name = "userId.provider", column = @Column(name = "PROVIDER"))
    })
    private UserId userId;

    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    @Column(name = "ARCHIVED_TIME", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime archivedTime;

    @Column(name = "ARCHIVED_REASON", nullable = false)
    private String archivedReason;

    public ArchivedUserEntity(UserId id, String userName, String archivedReason) {
        this.userId = id;
        this.userName = userName;
        this.archivedTime = LocalDateTime.now();; // 현재 시각으로 설정
        this.archivedReason = archivedReason;
    }

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        if (archivedTime == null) archivedTime = now;
    }

    //entity -> dto method
    public ArchivedUserDto toDto() {
        return ArchivedUserDto.builder()
                .userEmail(this.userId.getUserEmail())
                .provider(this.userId.getProvider())
                .userName(this.userName)
                .archivedTime(this.archivedTime)
                .archivedReason(this.archivedReason)
                .build();
    }
}