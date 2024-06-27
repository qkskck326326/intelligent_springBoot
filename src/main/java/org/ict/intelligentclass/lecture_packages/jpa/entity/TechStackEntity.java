package org.ict.intelligentclass.lecture_packages.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.model.dto.TechStackDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_TECH_STACK")
public class TechStackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_TECH_STACK_ID")
    @SequenceGenerator(name = "SQ_TECH_STACK_ID" , sequenceName = "SQ_TECH_STACK_ID", allocationSize = 1)
    @Column(name = "TECH_STACK_ID")
    private Long techStackId;

    @Column(name = "TECH_STACK_PATH", nullable = false)
    private String techStackPath;

    @Column(name = "TECH_STACK_NAME", nullable = false)
    private String techStackName;

    public TechStackDto toDto() {
        return TechStackDto.builder()
                .techStackId(this.techStackId)
                .techStackPath(this.techStackPath)
                .techStackName(this.techStackName)
                .build();
    }
}