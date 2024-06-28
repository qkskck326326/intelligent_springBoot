package org.ict.intelligentclass.post.jpa.repository;

import org.ict.intelligentclass.post.jpa.entity.PostTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostTagRepository extends JpaRepository<PostTagEntity, Long> {
}
