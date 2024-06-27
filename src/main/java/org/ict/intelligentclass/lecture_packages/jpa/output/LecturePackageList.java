package org.ict.intelligentclass.lecture_packages.jpa.output;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerDate;
    private int packageLevel;
    private int ratingId;
    private float rating;


}
