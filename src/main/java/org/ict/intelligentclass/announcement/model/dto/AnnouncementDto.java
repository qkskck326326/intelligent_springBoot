package org.ict.intelligentclass.announcement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.announcement.jpa.entity.AnnouncementEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class AnnouncementDto {

    private Long announcementId;
    private String title;
    private String content;
    private Date createdAt;
    private String creator;
    private Long category;
    private Long importance;

    public AnnouncementEntity toEntity() {
        return AnnouncementEntity.builder()
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
