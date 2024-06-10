package org.ict.intelligentclass.lecture.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture.jpa.entity.id.LectureReadId;
import org.ict.intelligentclass.lecture.model.dto.LectureReadDto;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_LECTURE_READ")
public class LectureReadEntity {

    @EmbeddedId
    private LectureReadId lectureReadId;

    // 유저와의 다대일(N:1) 관계 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "USEREMAIL", referencedColumnName = "userEmail"),
            @JoinColumn(name = "PROVIDER", referencedColumnName = "provider")
    })
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LECTURE_ID")
    private LectureEntity lecture;

    @Column(name = "LECTURE_READ", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N' CHECK (LECTURE_READ IN ('Y', 'N'))")
    private String lectureRead;

    // entity -> dto 변환 메서드 추가
    public LectureReadDto toDto() {
        return LectureReadDto.builder()
                .lectureId(lecture.getLectureId()) // 강의 식별자를 사용하여 LectureReadDto에 추가
                .nickname(user.getNickname())
                .lectureRead(lectureRead)
                .build();
    }
}
