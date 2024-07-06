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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecture_read_seq_generator")
    @SequenceGenerator(name = "lecture_read_seq_generator", sequenceName = "SQ_LECTURE_READ_ID", allocationSize = 1)
    @Column(name = "LECTURE_READ_ID")
    private int lectureReadId;

    @Column(name = "LECTURE_ID", nullable = false)
    private int lectureId;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "LECTURE_READ", columnDefinition = "CHAR(1)")
    private Long lectureRead;

    // entity -> dto 변환 메서드 추가
    public LectureReadDto toDto() {
        return LectureReadDto.builder()
                .lectureReadId(lectureReadId)
                .lectureId(lectureId)
                .nickname(nickname)
                .lectureRead(lectureRead)
                .build();
    }

    public void addLectureRead(Long additionalRead) {
        if (this.lectureRead == null) {
            this.lectureRead = additionalRead;
        } else {
            this.lectureRead += additionalRead;
        }
    }
}
