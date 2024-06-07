package org.ict.intelligentclass.announcement.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.ict.intelligentclass.announcement.model.dto.AnnouncementDto;
import org.ict.intelligentclass.itnewssite.model.dto.ItNewsSiteDto;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="TB_ANNOUNCEMENT")
@Builder
public class AnnouncementEntity {

    @Id
    @Column(name="ANNOUNCEMENT_ID")
    private Long announcementId;
    @Column(name="TITLE", nullable = false)
    private String title;
    @Column(name="CONTENT", nullable = false)
    private String content;
    @Column(name="CREATED_AT", nullable = false)
    private Date createdAt;
    @Column(name="CREATOR", nullable = false)
    private String creator;
    @Column(name="CATEGORY", nullable = false)
    private Long category;
    @Column(name="IMPORTANCE", nullable = false)
    private Long importance;

    public AnnouncementDto toDto() {
        return AnnouncementDto.builder()
                .announcementId(this.announcementId)
                .title(this.title)
                .content(this.content)
                .createdAt(this.createdAt)
                .creator(this.creator)
                .category(this.category)
                .importance(this.importance)
                .build();
    }
}
