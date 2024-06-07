package org.ict.intelligentclass.user.jpa.entity.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserInterestId implements Serializable {

    @Column(name = "USEREMAIL")
    private String userEmail;

    @Column(name = "PROVIDER")
    private String provider;

    @Column(name = "SUB_CATEGORY_ID")
    private Long subCategoryId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInterestId that = (UserInterestId) o;
        return subCategoryId == that.subCategoryId && Objects.equals(userEmail, that.userEmail) && Objects.equals(provider, that.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, provider, subCategoryId);
    }
}