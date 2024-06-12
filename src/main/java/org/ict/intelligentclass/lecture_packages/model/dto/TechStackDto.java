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
    private String techStackPath;
    private String techStackName;

    public TechStackEntity toEntity() {
        return TechStackEntity.builder()
                .techStackId(this.techStackId)
                .techStackPath(this.techStackPath)
                .techStackName(this.techStackName)
                .build();
    }
}