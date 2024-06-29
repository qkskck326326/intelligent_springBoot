package org.ict.intelligentclass.post.jpa.repository;

import org.ict.intelligentclass.post.jpa.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Long> {
    List<BookmarkEntity> findByUserEmailAndProvider(String userEmail, String provider);
    Optional<BookmarkEntity> findByPostIdAndUserEmailAndProvider(Long postId, String userEmail, String provider);
}
