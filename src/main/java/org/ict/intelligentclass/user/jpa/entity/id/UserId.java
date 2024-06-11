package org.ict.intelligentclass.user.jpa.entity.id;

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
public class UserId implements Serializable {
    private String userEmail;
    private String provider;

    // equals()와 hashCode() 메소드
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserId userId = (UserId) o;
        return Objects.equals(userEmail, userId.userEmail) &&
                Objects.equals(provider, userId.provider);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userEmail, provider);
    }

}