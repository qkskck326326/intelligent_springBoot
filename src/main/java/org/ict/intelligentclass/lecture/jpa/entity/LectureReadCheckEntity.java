package org.ict.intelligentclass.lecture.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.model.dto.LectureReadCheckDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_LECTURE_READ_CHECK")
public class LectureReadCheckEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lecture_read_check_seq_generator")
    @SequenceGenerator(name = "lecture_read_check_seq_generator", sequenceName = "SQ_LECTURE_READ_CHECK_ID", allocationSize = 1)
    @Column(name = "LECTURE_READ_CHECK_ID")
    private int lectureReadCheckId;

    @Column(name = "LECTURE_ID", nullable = false)
    private int lectureId;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "READ_CHECK", columnDefinition = "CHAR(1)")
    private String readCheck;

    // entity -> dto 변환 메서드 추가
    public LectureReadCheckDto toDto() {
        return LectureReadCheckDto.builder()
                .lectureReadCheckId(lectureReadCheckId)
                .lectureId(lectureId)
                .nickname(nickname)
                .readCheck(readCheck)
                .build();
    }
}
