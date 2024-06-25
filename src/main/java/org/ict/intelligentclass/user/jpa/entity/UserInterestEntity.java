package org.ict.intelligentclass.user.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserInterestId;
import org.ict.intelligentclass.user.model.dto.UserInterestDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_USER_INTEREST")
public class UserInterestEntity {

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "interestId.userEmail", column = @Column(name = "USEREMAIL")),
            @AttributeOverride(name = "interestId.provider", column = @Column(name = "PROVIDER")),
            @AttributeOverride(name = "interestId.subCategoryId", column = @Column(name = "SUB_CATEGORY_ID"))
    })
    private UserInterestId interestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumns({
            @JoinColumn(name = "USEREMAIL", referencedColumnName = "userEmail"),
            @JoinColumn(name = "PROVIDER", referencedColumnName = "provider")
    })
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("subCategoryId")
    @JoinColumn(name = "SUB_CATEGORY_ID")
    private SubCategoryEntity subCategory;

    public UserInterestDto toDto() {
        return UserInterestDto.builder()
                .userEmail(this.interestId.getUserEmail())
                .provider(this.interestId.getProvider())
                .subCategoryId(this.interestId.getSubCategoryId())
                .build();
    }
}