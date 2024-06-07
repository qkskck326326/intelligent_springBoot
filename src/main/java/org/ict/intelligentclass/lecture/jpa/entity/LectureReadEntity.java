package org.ict.intelligentclass.lecture.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.model.dto.LectureReadDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_LECTURE_READ")
public class LectureReadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LECTURE_ID")
    private int lectureId;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "PACKAGE_ID", nullable = false)
    private int packageId;

    @Column(name = "LECTURE_READ", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N' CHECK (LECTURE_READ IN ('Y', 'N'))")
    private String lectureRead;

    // entity -> dto 변환 메서드 추가
    public LectureReadDto toDto() {
        return LectureReadDto.builder()
                .lectureId(lectureId)
                .nickname(nickname)
                .packageId(packageId)
                .lectureRead(lectureRead)
                .build();
    }
}
