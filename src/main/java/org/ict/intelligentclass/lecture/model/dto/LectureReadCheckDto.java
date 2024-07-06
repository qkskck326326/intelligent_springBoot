package org.ict.intelligentclass.lecture.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureReadCheckEntity;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class LectureReadCheckDto {

    private int lectureReadCheckId;
    private int lectureId;
    private String nickname;
    private String readCheck;

    public LectureReadCheckEntity toEntity() {
        return LectureReadCheckEntity.builder()
                .lectureReadCheckId(this.lectureReadCheckId)
                .lectureId(this.lectureId)
                .nickname(this.nickname)
                .readCheck(this.readCheck)
                .build();
    }

}
