package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;

// 실험용
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackageRatingDto {
    private Long lecturePackageId;
    private float rating;

    public PackageRatingDto(RatingEntity ratingEntity) {
        this.lecturePackageId = ratingEntity.getLecturePackageId();
        this.rating = ratingEntity.getRating();
    }
}
