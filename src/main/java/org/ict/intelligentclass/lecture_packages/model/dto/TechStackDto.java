package org.ict.intelligentclass.lecture_packages.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.TechStackEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TechStackDto {
    private Long techStackId;
    private String path;
    private String techStackName;

    public static TechStackDto fromEntity(TechStackEntity entity) {
        return TechStackDto.builder()
                .techStackId(entity.getTechStackId())
                .path(entity.getPath())
                .techStackName(entity.getTechStackName())
                .build();
    }

    public TechStackEntity toEntity() {
        return TechStackEntity.builder()
                .techStackId(this.techStackId)
                .path(this.path)
                .techStackName(this.techStackName)
                .build();
    }
}