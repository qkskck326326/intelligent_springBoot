package org.ict.intelligentclass.lecture_packages.jpa.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageSubCategoryId;
import org.ict.intelligentclass.lecture_packages.jpa.entity.PackageTechStackId;

import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturePackageDetail {

    private Long lecturePackageId;
    private String nickname;
    private String title;
    private String classGoal;
    private String recommendPerson;
    private int priceKind;
    private int price;
    private String thumbnail;
    private Date register;
    private int viewCount;
    private List<Long> subCategoryId;
    private String subCategoryName;
    private List<Long> techStackId;
    private String techStackPath;

}
