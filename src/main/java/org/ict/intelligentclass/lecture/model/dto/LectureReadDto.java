package org.ict.intelligentclass.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureReadEntity;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class LectureReadDto {

    private int lectureReadId;
    private int lectureId;
    private String nickname;
    private String lectureRead;

    public LectureReadEntity toEntity() {
        return LectureReadEntity.builder()
                .lectureReadId(this.lectureReadId)
                .lectureId(this.lectureId)
                .nickname(this.nickname)
                .lectureRead(this.lectureRead)
                .build();
    }

}
