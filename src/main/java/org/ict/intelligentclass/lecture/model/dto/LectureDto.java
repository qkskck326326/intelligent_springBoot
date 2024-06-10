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
    private String streamUrl;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date lectureDate;
    private Long lecturePackageId;
    private String nickname;
    private int lectureViewCount;



    public LectureEntity toEntity() {
        return LectureEntity.builder()
                .lectureId(this.lectureId)
                .lectureName(this.lectureName)
                .lectureContent(this.lectureContent)
                .lectureThumbnail(this.lectureThumbnail)
                .streamUrl(this.streamUrl)
                .lectureDate(this.lectureDate)
                .lecturePackageId(this.lecturePackageId)
                .nickname(this.nickname)
                .lectureViewCount(this.lectureViewCount)
                .build();
    }
}
