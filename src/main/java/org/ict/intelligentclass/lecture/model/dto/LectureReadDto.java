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

    private int lectureId;
    private String nickname;
    private int packageId;
    private String lectureRead;

    // 엔티티로 변환하는 메서드
    public LectureReadEntity toEntity() {
        return LectureReadEntity.builder()
                .lectureId(this.lectureId)
                .nickname(this.nickname)
                .packageId(this.packageId)
                .lectureRead(this.lectureRead)
                .build();
    }
}
