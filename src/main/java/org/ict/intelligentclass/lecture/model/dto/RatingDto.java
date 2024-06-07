package org.ict.intelligentclass.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class RatingDto {

    private int ratingId;
    private String nickname;
    private int packageId;
    private Float rating;

    // 엔티티로 변환하는 메서드
    public RatingEntity toEntity() {
        return RatingEntity.builder()
                .ratingId(this.ratingId)
                .nickname(this.nickname)
                .packageId(this.packageId)
                .rating(this.rating)
                .build();
    }
}
