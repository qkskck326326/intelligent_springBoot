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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LECTURE_ID")
    private int lectureId;

    @Column(name = "LECTURE_NAME", nullable = false)
    private String lectureName;

    @Column(name = "LECTURE_CONTENT", nullable = false, length = 1000)
    private String lectureContent;

    @Column(name = "LECTURE_THUMBNAIL")
    private String lectureThumbnail;

    @Lob
    @Column(name = "LECTURE_VIDEO", nullable = false)
    private byte[] lectureVideo;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LECTURE_DATE", nullable = false)
    private Date lectureDate;

    @Column(name = "PACKAGE_ID", nullable = false)
    private int packageId;

    @Column(name = "NICKNAME")
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
                .lectureVideo(lectureVideo)
                .lectureDate(lectureDate)
                .packageId(packageId)
                .nickname(nickname)
                .lectureViewCount(lectureViewCount)
                .build();
    }

}
