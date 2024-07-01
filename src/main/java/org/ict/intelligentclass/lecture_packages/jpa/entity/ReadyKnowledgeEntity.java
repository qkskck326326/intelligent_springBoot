package org.ict.intelligentclass.lecture_packages.jpa.entity;


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
@Table(name = "TB_READY_KNOWLEDGE")
public class ReadyKnowledgeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_READY_KNOWLEDGE_ID")
    @SequenceGenerator(name = "SQ_READY_KNOWLEDGE_ID", sequenceName = "SQ_READY_KNOWLEDGE_ID", allocationSize = 1)
    @Column(name = "READY_KNOWLEDGE_ID")
    private Long readyKnowledgeId;

    @ManyToOne
    @JoinColumn(name = "LECTURE_PACKAGE_ID", nullable = false)
    private LecturePackageEntity lecturePackage;

    @Column(name = "READY_CONTENT", nullable = false, length = 60)
    private String readyContent;
}


