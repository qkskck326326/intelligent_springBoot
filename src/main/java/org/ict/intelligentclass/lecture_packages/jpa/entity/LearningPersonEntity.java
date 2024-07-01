package org.ict.intelligentclass.lecture_packages.jpa.entity;

import jakarta.persistence.Table;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_LEARNING_PERSON")
public class LearningPersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_LEARNING_PERSON_ID")
    @SequenceGenerator(name = "SQ_LEARNING_PERSON_ID", sequenceName = "SQ_LEARNING_PERSON_ID", allocationSize = 1)
    @Column(name = "LEARNING_PERSON_ID")
    private Long learningPersonId;

    @ManyToOne
    @JoinColumn(name = "LECTURE_PACKAGE_ID", nullable = false)
    private LecturePackageEntity lecturePackage;

    @Column(name = "LEARNING_CONTENT", nullable = false, length = 60)
    private String learningContent;
}