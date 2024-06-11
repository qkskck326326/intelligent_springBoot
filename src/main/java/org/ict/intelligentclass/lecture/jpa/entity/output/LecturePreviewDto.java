package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturePreviewDto {
    private int lectureId;
    private String nickname;
    private String lectureName;
    private String lectureContent;
    private String lectureThumbnail;

    public LecturePreviewDto(LectureEntity lectureEntity) {
        this.lectureId = lectureEntity.getLectureId();
        this.nickname = lectureEntity.getNickname();
        this.lectureName = lectureEntity.getLectureName();
        this.lectureContent = lectureEntity.getLectureContent();
        this.lectureThumbnail = lectureEntity.getLectureThumbnail();
    }
}
