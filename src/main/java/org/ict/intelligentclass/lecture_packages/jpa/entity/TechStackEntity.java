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
@Table(name = "TB_TECH_STACK")
public class TechStackEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TECH_STACK_ID")
    private Long techStackId;

    @Column(name = "TECH_STACK_PATH", nullable = false)
    private String path;

    @Column(name = "TECH_STACK_NAME", nullable = false)
    private String techStackName;
}