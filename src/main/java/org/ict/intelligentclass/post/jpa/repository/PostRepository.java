package org.ict.intelligentclass.post.jpa.repository;

import org.ict.intelligentclass.post.jpa.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p, u, sc FROM PostEntity p " +
            "LEFT JOIN UserEntity u ON p.userEmail = u.userId.userEmail AND p.provider = u.userId.provider " +
            "LEFT JOIN SubCategoryEntity sc ON p.subCategoryId = sc.id " +
            "WHERE p.id = :postId")
    Optional<PostEntity> findPostWithUserAndSubCategoryById(@Param("postId") Long postId);

    List<PostEntity> findByTitleContainingOrContentContaining(String keyword, String keyword1);

    List<PostEntity> findBySubCategoryId(Long subCategoryId);


}




