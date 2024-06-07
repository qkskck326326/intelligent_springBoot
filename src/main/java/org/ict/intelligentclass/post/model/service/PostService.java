package org.ict.intelligentclass.post.model.service;

import org.ict.intelligentclass.post.jpa.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private SubCategoryRepository subCategoryRepository;
//    public Page<PostDto> getAllPosts(int page, int size, String sortBy) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
//        Page<PostEntity> posts = postRepository.findAll(pageable);
//
//        return posts.map(PostEntity::toDto);
//    }
}
