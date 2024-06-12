package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDetailDto {
    private int lectureId;
    private String nickname;
    private String lectureName;
    private int lectureViewCount;
    private String streamUrl;

    public LectureDetailDto(LectureEntity lectureEntity) {
        this.lectureId = lectureEntity.getLectureId();
        this.nickname = lectureEntity.getNickname();
        this.lectureName = lectureEntity.getLectureName();
        this.lectureViewCount = lectureEntity.getLectureViewCount();
        this.streamUrl = lectureEntity.getStreamUrl();
    }
}
