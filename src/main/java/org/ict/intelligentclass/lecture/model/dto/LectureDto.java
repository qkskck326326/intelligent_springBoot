package org.ict.intelligentclass.lecture.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component
public class LectureDto {

    private int lectureId;
    private String lectureName;
    private String lectureContent;
    private String lectureThumbnail;
    private byte[] lectureVideo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date lectureDate;
    private int packageId;
    private String nickname;
    private int lectureViewCount;

    public LectureEntity toEntity() {
        return LectureEntity.builder()
                .lectureId(this.lectureId)
                .lectureName(this.lectureName)
                .lectureContent(this.lectureContent)
                .lectureThumbnail(this.lectureThumbnail)
                .lectureVideo(this.lectureVideo)
                .lectureDate(this.lectureDate)
                .packageId(this.packageId)
                .nickname(this.nickname)
                .lectureViewCount(this.lectureViewCount)
                .build();
    }
}
