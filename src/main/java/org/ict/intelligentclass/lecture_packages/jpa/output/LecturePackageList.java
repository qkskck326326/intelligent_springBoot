package org.ict.intelligentclass.lecture_packages.jpa.output;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageSubCategoryId;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturePackageList {

    private Long lecturePackageId;
    private String nickname;
    private String title;
    private String thumbnail;
    private int viewCount;
    private int ratingId;
    private float rating;
}
