package org.ict.intelligentclass.lecture_packages.jpa.output;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String content;
    private int priceKind;
    private int priceMonth;
    private int priceForever;
    private int packageLevel;
    private String thumbnail;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date registerDate;
    private int viewCount;
    private String backgroundColor;
    private List<Long> subCategoryId;
    private String subCategoryName;
    private List<Long> techStackId;
    private String techStackPath;

}
