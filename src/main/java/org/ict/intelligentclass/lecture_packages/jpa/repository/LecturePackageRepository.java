package org.ict.intelligentclass.lecture_packages.jpa.repository;


import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturePackageRepository extends JpaRepository<LecturePackageEntity, Long> {



//    @Query("SELECT l FROM LecturePackageEntity l WHERE l.lecturePackageId = :packageId")
//    List<LecturePackageEntity> findByCategorySortPackages(@Param("packageId") Long packageId);



    @Query("SELECT l FROM LecturePackageEntity l WHERE l.lecturePackageId IN :packageIds")
    List<LecturePackageEntity> findByLecturePackageIdIn(@Param("packageIds") List<Long> packageIds);

    @Query("SELECT lp FROM LecturePackageEntity lp WHERE lp.title LIKE %:searchTerm% ")
    Page<LecturePackageEntity> searchByTitle(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT lp FROM LecturePackageEntity lp WHERE lp.nickname LIKE %:searchTerm%")
    Page<LecturePackageEntity> searchByInstructor(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT lp FROM LecturePackageEntity lp LEFT JOIN RatingEntity r ON lp.lecturePackageId = r.lecturePackageId ORDER BY CASE WHEN r.rating IS NULL THEN 1 ELSE 0 END, r.rating DESC")
    Page<LecturePackageEntity> findAllOrderByRating(Pageable pageable);

//    @Query("SELECT l FROM LecturePackageEntity l WHERE l.title LIKE %:searchTerm%")
//    Page<LecturePackageEntity> searchByTitle(@Param("searchTerm") String searchTerm, Pageable pageable);
//
//    @Query("SELECT l FROM LecturePackageEntity l WHERE l.nickname LIKE %:searchTerm%")
//    Page<LecturePackageEntity> searchByInstructor(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT l FROM LecturePackageEntity l JOIN l.packageSubCategory p WHERE p.subCategory.id = :subCategoryId")
    Page<LecturePackageEntity> findBySubCategoryId(@Param("subCategoryId") Long subCategoryId, Pageable pageable);

    @Query("SELECT l FROM LecturePackageEntity l JOIN l.packageSubCategory p WHERE p.subCategory.id = :subCategoryId ORDER BY (SELECT COALESCE(AVG(r.rating), 0) FROM RatingEntity r WHERE r.lecturePackageId = l.lecturePackageId) DESC")
    Page<LecturePackageEntity> findBySubCategoryIdOrderByRating(@Param("subCategoryId") Long subCategoryId, Pageable pageable);





//    @Query("SELECT l FROM LecturePackageEntity l ORDER BY l.rating DESC")
//    Page<LecturePackageEntity> findAllOrderByRating(Pageable pageable);








}