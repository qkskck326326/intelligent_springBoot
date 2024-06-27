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

    // 검색어가 제목이면서 최신순일때
    @Query("SELECT l FROM LecturePackageEntity l WHERE l.title LIKE %:searchTerm% ORDER BY l.registerDate desc ")
    Page<LecturePackageEntity> searchByTitleRegister(@Param("searchTerm") String searchTerm, Pageable pageable);

    // 검색어가 제목이면서 별점순일때
    @Query("SELECT l FROM LecturePackageEntity l WHERE l.title LIKE %:searchTerm% ORDER BY (SELECT COALESCE(AVG(r.rating), 0) FROM RatingEntity r WHERE r.lecturePackageId = l.lecturePackageId) DESC")
    Page<LecturePackageEntity> searchByTitleByRating(@Param("searchTerm") String searchTerm, Pageable pageable);

    // 검색어가 제목이면서 조회순일때
    @Query("SELECT l FROM LecturePackageEntity l WHERE l.title LIKE %:searchTerm% ORDER BY l.viewCount DESC")
    Page<LecturePackageEntity> searchByTitleByViewCount(@Param("searchTerm") String searchTerm, Pageable pageable);


    // 검색어가 닉네임일때
    @Query("SELECT l FROM LecturePackageEntity l WHERE l.nickname LIKE %:searchTerm%")
    Page<LecturePackageEntity> searchByInstructorRegister(@Param("searchTerm") String searchTerm, Pageable pageable);

    // 검색어가 닉네임이면서 별점순일때
    @Query("SELECT l FROM LecturePackageEntity l WHERE l.nickname LIKE %:searchTerm% ORDER BY (SELECT COALESCE(AVG(r.rating), 0) FROM RatingEntity r WHERE r.lecturePackageId = l.lecturePackageId) DESC")
    Page<LecturePackageEntity> searchByInstructorByRating(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT l FROM LecturePackageEntity l WHERE l.nickname LIKE %:searchTerm% ORDER BY l.viewCount DESC")
    Page<LecturePackageEntity> searchByInstructorByViewCount(@Param("searchTerm") String searchTerm, Pageable pageable);



    @Query("SELECT l FROM LecturePackageEntity l LEFT JOIN RatingEntity r ON l.lecturePackageId = r.lecturePackageId ORDER BY (SELECT COALESCE(AVG(r.rating), 0) FROM RatingEntity r WHERE r.lecturePackageId = l.lecturePackageId) DESC")
    Page<LecturePackageEntity> findAllOrderByRating(Pageable pageable);

//    @Query("SELECT l FROM LecturePackageEntity l JOIN l.packageSubCategory p WHERE p.subCategory.id = :subCategoryId")
//    Page<LecturePackageEntity> findBySubCategoryId(@Param("subCategoryId") Long subCategoryId, Pageable pageable);

    //선택한 카테고리의 별점순
    @Query("SELECT l FROM LecturePackageEntity l JOIN l.packageSubCategory p WHERE p.packageSubCategoryId.subCategoryId = :subCategoryId ORDER BY (SELECT COALESCE(AVG(r.rating), 0) FROM RatingEntity r WHERE r.lecturePackageId = l.lecturePackageId) DESC")
    Page<LecturePackageEntity> findBySubCategoryIdOrderByRating(@Param("subCategoryId") Long subCategoryId, Pageable pageable);


    // 조회순
    @Query("SELECT l FROM LecturePackageEntity l ORDER BY l.viewCount DESC")
    Page<LecturePackageEntity> findAllOrderByViewCount(Pageable pageable);

    // 최신순
    @Query("SELECT l FROM LecturePackageEntity l ORDER BY l.registerDate DESC")
    Page<LecturePackageEntity> findAllOrderByLatest(Pageable pageable);

    // 서브카테고리별 조회순
    @Query("SELECT l FROM LecturePackageEntity l JOIN l.packageSubCategory p WHERE p.packageSubCategoryId.subCategoryId = :subCategoryId ORDER BY l.viewCount DESC")
    Page<LecturePackageEntity> findBySubCategoryIdOrderByViewCount(@Param("subCategoryId") Long subCategoryId, Pageable pageable);

    // 서브카테고리별 최신순
    @Query("SELECT l FROM LecturePackageEntity l JOIN l.packageSubCategory p WHERE p.packageSubCategoryId.subCategoryId = :subCategoryId ORDER BY l.registerDate DESC")
    Page<LecturePackageEntity> findBySubCategoryIdOrderByLatest(@Param("subCategoryId") Long subCategoryId, Pageable pageable);
}