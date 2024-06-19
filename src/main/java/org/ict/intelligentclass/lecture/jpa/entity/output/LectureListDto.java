package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.ict.intelligentclass.lecture.jpa.entity.LectureReadEntity;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureListDto {
    private int lectureId;
    private String nickname;
    private String lectureName;

    public LectureListDto(LectureEntity lectureEntity) {
        this.lectureId = lectureEntity.getLectureId();
        this.nickname = lectureEntity.getNickname();
        this.lectureName = lectureEntity.getLectureName();
    }
}
