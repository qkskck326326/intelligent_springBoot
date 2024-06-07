package org.ict.intelligentclass.post.jpa.repository;

import org.ict.intelligentclass.post.jpa.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

}
