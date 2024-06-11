package org.ict.intelligentclass.lecture.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.model.dto.RatingDto;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_RATING")
public class RatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RATING_ID")
    private int ratingId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "LECTURE_PACKAGE_ID")
    private Long lecturePackageId;

    @Column(name = "RATING", nullable = false)
    private Float rating;

    // entity -> dto 변환 메서드 추가
    public RatingDto toDto() {
        return RatingDto.builder()
                .ratingId(ratingId)
                .nickname(nickname)
                .lecturePackageId(lecturePackageId)
                .rating(rating)
                .build();
    }
}
