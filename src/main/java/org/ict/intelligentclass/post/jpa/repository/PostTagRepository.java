package org.ict.intelligentclass.post.jpa.repository;

import org.ict.intelligentclass.post.jpa.entity.PostTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTagEntity, Long> {
    List<PostTagEntity> findByIdPostId(Long postId);

    void deleteByPostId(Long id);
}
