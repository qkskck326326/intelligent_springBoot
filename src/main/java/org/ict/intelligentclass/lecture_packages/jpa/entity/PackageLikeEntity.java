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
@Table(name = "TB_PACKAGE_LIKE")
public class PackageLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PACKAGE_LIKE_ID")
    @SequenceGenerator(name = "SQ_PACKAGE_LIKE_ID", sequenceName = "SQ_PACKAGE_LIKE_ID", allocationSize = 1)
    @Column(name = "PACKAGE_LIKE_ID")
    private Long packageLikeId;


    @Column(name = "LECTURE_PACKAGE_ID", nullable = false)
    private Long lecturePackageId;


    @Column(name = "NICKNAME", nullable = false)
    private String nickname;
}
