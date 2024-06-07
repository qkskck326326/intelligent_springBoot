package org.ict.intelligentclass.lecture_packages.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageSubCategoryId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageSubCategoryIdDto {
    private Long lecturePackageId;
    private Long subCategoryId;
}