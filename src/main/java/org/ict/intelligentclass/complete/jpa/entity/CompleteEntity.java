package org.ict.intelligentclass.complete.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_COMPLETE")
public class CompleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_COMPLETE_ID")
    @SequenceGenerator(name = "SQ_COMPLETE_ID", sequenceName = "SQ_COMPLETE_ID", allocationSize = 1)
    @Column(name = "COMPLETE_ID")
    private Long completeId;

    @Column(name = "NICKNAME", nullable = false)
    private String nickname;

    @Column(name = "LECTURE_PACKAGE_ID", nullable = false)
    private Long lecturePackageId;

    @Column(name = "COMPLETE_PDF")
    private String completePdf;
}