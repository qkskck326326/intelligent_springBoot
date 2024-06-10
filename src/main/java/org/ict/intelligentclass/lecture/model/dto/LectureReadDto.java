package org.ict.intelligentclass.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureReadEntity;
import org.ict.intelligentclass.lecture.jpa.entity.id.LectureReadId;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class LectureReadDto {

    private int lectureId; // lectureId 필드의 타입을 Long으로 변경
    private String nickname;
    private Long lecturePackageId;
    private String lectureRead;

    // 엔티티로 변환하는 메서드
    public LectureReadEntity toEntity() {
        LectureReadId lectureReadId = new LectureReadId(); // LectureReadId 객체 생성
        lectureReadId.setLectureId(this.lectureId); // lectureId 설정
        lectureReadId.setNickname(this.nickname); // nickname 설정

        return LectureReadEntity.builder()
                .lectureReadId(lectureReadId) // LectureReadId 객체를 사용하여 설정
                .lectureRead(this.lectureRead)
                .build();
    }

}
