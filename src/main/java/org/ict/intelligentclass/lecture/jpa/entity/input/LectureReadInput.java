package org.ict.intelligentclass.lecture.jpa.entity.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureReadInput {

    private int lectureId;
    private String nickname;
    private Long lectureRead;
}
