package org.ict.intelligentclass.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.UserInterestEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserInterestId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInterestDto {
    private String userEmail;
    private String provider;
    private Long subCategoryId;

    public UserInterestEntity toEntity(UserEntity userEntity) {
        SubCategoryEntity subCategory = SubCategoryEntity.builder()
                .id(this.subCategoryId)
                .build();

        return UserInterestEntity.builder()
                .interestId(new UserInterestId(this.userEmail, this.provider, this.subCategoryId))
                .user(userEntity)
                .subCategory(subCategory)
                .build();
    }
}