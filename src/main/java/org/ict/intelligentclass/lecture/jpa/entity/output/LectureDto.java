package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;

// 실험용
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDto {
    private String lectureName;
    private String lectureContent;
    private String lectureThumbnail;
    private String streamUrl;

    public LectureDto(LectureEntity lectureEntity) {
        this.lectureName = lectureEntity.getLectureName();
        this.lectureContent = lectureEntity.getLectureContent();
        this.lectureThumbnail = lectureEntity.getLectureThumbnail();
        this.streamUrl = lectureEntity.getStreamUrl();
    }
}
