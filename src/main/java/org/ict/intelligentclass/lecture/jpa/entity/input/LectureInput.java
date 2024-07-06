package org.ict.intelligentclass.lecture.jpa.entity.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureInput {
    private String lectureName;
    private String lectureContent;
    private String lectureThumbnail;
    private String streamUrl;
    private Long lecturePackageId;
    private String nickname;
    private Long longVideo;
}
