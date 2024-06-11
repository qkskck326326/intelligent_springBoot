package org.ict.intelligentclass.lecture.jpa.entity.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.ict.intelligentclass.lecture.jpa.entity.LectureReadEntity;


//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class LectureListDto {
//    private int lectureId;
//    private String nickname;
//    private String lectureName;
//    private String lectureRead;
//    private String lecturePackageId;
//
//    public LectureListDto(LectureEntity lectureEntity, LectureReadEntity lectureReadEntity) {
//        this.lectureId = lectureEntity.getLectureId();
//        this.nickname = lectureEntity.getNickname();
//        this.lectureName = lectureEntity.getLectureName();
//        this.lectureRead = lectureReadEntity.getLectureRead();
//        this.lecturePackageId = lectureEntity.getLecturePackageId();
//    }
//}

// 실험용
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureListDto {
    private int lectureId;
    private String nickname;
    private String lectureName;
    private String lectureRead;

    public LectureListDto(LectureEntity lectureEntity, LectureReadEntity lectureReadEntity) {
        this.lectureId = lectureEntity.getLectureId();
        this.nickname = lectureEntity.getNickname();
        this.lectureName = lectureEntity.getLectureName();
        this.lectureRead = lectureReadEntity.getLectureRead();
    }
}
