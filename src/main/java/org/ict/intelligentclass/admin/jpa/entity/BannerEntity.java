package org.ict.intelligentclass.admin.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.ict.intelligentclass.admin.model.dto.BannerDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_banners")
@Getter
@Setter
public class BannerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static BannerEntity fromDto(BannerDto dto) {
        BannerEntity entity = new BannerEntity();
        entity.setTitle(dto.getTitle());
        entity.setImageUrl(dto.getImageUrl());
        entity.setLinkUrl(dto.getLinkUrl());
        return entity;
    }

    public BannerDto toDto() {
        BannerDto dto = new BannerDto();
        dto.setId(this.id);
        dto.setTitle(this.title);
        dto.setImageUrl(this.imageUrl);
        dto.setLinkUrl(this.linkUrl);
        return dto;
    }

    public void updateFromDto(BannerDto dto) {
        this.title = dto.getTitle();
        this.imageUrl = dto.getImageUrl();
        this.linkUrl = dto.getLinkUrl();
    }
}
