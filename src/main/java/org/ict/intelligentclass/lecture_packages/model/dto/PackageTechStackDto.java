package org.ict.intelligentclass.lecture_packages.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageTechStackEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageTechStackId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageTechStackDto {
    private PackageTechStackIdDto id;
    private TechStackDto techStack;

    public static PackageTechStackDto fromEntity(PackageTechStackEntity entity) {
        return PackageTechStackDto.builder()
                .id(new PackageTechStackIdDto(entity.getId().getLecturePackageId(), entity.getId().getTechStackId()))
                .techStack(TechStackDto.fromEntity(entity.getTechStack()))
                .build();
    }

    public PackageTechStackEntity toEntity() {
        PackageTechStackEntity entity = new PackageTechStackEntity();
        entity.setId(new PackageTechStackId(this.id.getLecturePackageId(), this.id.getTechStackId()));
        entity.setTechStack(this.techStack.toEntity());
        return entity;
    }
}