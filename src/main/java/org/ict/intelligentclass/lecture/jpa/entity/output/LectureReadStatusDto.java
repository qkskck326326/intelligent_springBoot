package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.ict.intelligentclass.lecture.jpa.entity.LectureReadEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureReadStatusDto {

    private int lectureId;
    private String nickname;
    private Long lectureRead;
    private Long longVideo;

    public LectureReadStatusDto(LectureReadEntity lectureReadEntity, LectureEntity lectureEntity) {
        this.lectureId = lectureReadEntity.getLectureId();
        this.nickname = lectureReadEntity.getNickname();
        this.lectureRead = lectureReadEntity.getLectureRead();
        this.longVideo = lectureEntity.getLongVideo();
    }
}
