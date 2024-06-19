package org.ict.intelligentclass.post.jpa.repository;

import org.ict.intelligentclass.post.jpa.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    @Query("SELECT COUNT(c) FROM CommentEntity c WHERE c.postId = :postId")
    long countByPostId(@Param("postId") Long postId);
    @Query("SELECT c FROM CommentEntity c WHERE c.postId = :postId")
    List<CommentEntity> findCommentsWithUserByPostId(@Param("postId") Long postId);

    void deleteByPostId(Long postId);


}
