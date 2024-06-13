package org.ict.intelligentclass.lecture.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.model.dto.LectureDto;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TB_LECTURE")
@Entity
public class LectureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecture_seq_generator")
    @SequenceGenerator(name = "lecture_seq_generator", sequenceName = "SQ_LECTURE_ID", allocationSize = 1)
    @Column(name = "LECTURE_ID")
    private int lectureId;

    @Column(name = "LECTURE_NAME", nullable = false)
    private String lectureName;

    @Column(name = "LECTURE_CONTENT", nullable = false, length = 1000)
    private String lectureContent;

    @Column(name = "LECTURE_THUMBNAIL")
    private String lectureThumbnail;

    @Column(name = "STREAM_URL", nullable = false)
    private String streamUrl;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LECTURE_DATE", nullable = false)
    private Date lectureDate;

    @Column(name = "LECTURE_PACKAGE_ID", nullable = false)
    private Long lecturePackageId;

    @Column(name = "NICKNAME", nullable = false, length = 50)
    private String nickname;

    @Column(name = "LECTURE_VIEWCOUNT", nullable = false, columnDefinition = "NUMBER DEFAULT 0")
    private int lectureViewCount;

    @PrePersist
    public void prePersist() {
        this.lectureDate = this.lectureDate == null ? new Date() : this.lectureDate;
    }

    // entity -> dto 변환 메서드 추가
    public LectureDto toDto() {
        return LectureDto.builder()
                .lectureId(lectureId)
                .lectureName(lectureName)
                .lectureContent(lectureContent)
                .lectureThumbnail(lectureThumbnail)
                .streamUrl(streamUrl)
                .lectureDate(lectureDate)
                .nickname(nickname)
                .lectureViewCount(lectureViewCount)
                .build();
    }
}
