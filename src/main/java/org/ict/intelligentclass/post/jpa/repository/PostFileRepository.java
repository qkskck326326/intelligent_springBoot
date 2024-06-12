package org.ict.intelligentclass.post.jpa.repository;

import org.ict.intelligentclass.post.jpa.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFileRepository  extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByPostId(Long postId);
}
