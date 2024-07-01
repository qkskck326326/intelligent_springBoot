package org.ict.intelligentclass.post.jpa.repository;

import org.ict.intelligentclass.post.jpa.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    TagEntity findByName(String name);

    @Query("SELECT t.name FROM TagEntity t JOIN PostTagEntity pt ON t.id = pt.tag.id GROUP BY t.name ORDER BY COUNT(pt.post.id) DESC")
    List<String> findTop10PopularTags();
}
