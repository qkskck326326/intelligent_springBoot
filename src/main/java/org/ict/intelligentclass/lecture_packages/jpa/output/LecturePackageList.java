package org.ict.intelligentclass.lecture_packages.jpa.output;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


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
    private Date registerDate;
    private int packageLevel;
    private int ratingId;
    private float rating;


}
