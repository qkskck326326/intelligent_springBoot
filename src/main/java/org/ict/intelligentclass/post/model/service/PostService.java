package org.ict.intelligentclass.post.model.service;

import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.exception.PostNotFoundException;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.lecture_packages.jpa.repository.SubCategoryRepository;
import org.ict.intelligentclass.post.jpa.entity.CommentEntity;
import org.ict.intelligentclass.post.jpa.entity.FileEntity;
import org.ict.intelligentclass.post.jpa.entity.PostEntity;


import org.ict.intelligentclass.post.jpa.repository.*;
import org.ict.intelligentclass.post.model.dto.PostDetailDto;
import org.ict.intelligentclass.post.model.dto.PostDto;
import org.ict.intelligentclass.post.storage.FileStorageService;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostFileRepository fileRepository;
    @Autowired
    private FileStorageService fileStorageService;

    public Page<PostDto> getAllPosts(Pageable pageable) {
        log.info("Fetching all posts with pageable: {}", pageable);
        Pageable sortedByDateDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("postTime").descending()
        );
        Page<PostEntity> posts = postRepository.findAll(sortedByDateDesc);
        return posts.map(post -> {
            PostDto dto = new PostDto();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContentSnippet(post.getContent().substring(0, Math.min(post.getContent().length(), 80)));
            // UserEntity 정보 가져오기
            Optional<UserEntity> userOptional = userRepository.findById(new UserId(post.getUserEmail(), post.getProvider()));
            userOptional.ifPresent(user -> dto.setNickname(user.getNickname()));
            // SubCategoryEntity 정보 가져오기
            Optional<SubCategoryEntity> subCategoryOptional = subCategoryRepository.findById(post.getSubCategoryId());
            subCategoryOptional.ifPresent(subCategory -> dto.setCategoryName(subCategory.getName()));
            // Like and Comment Count 가져오기
            dto.setLikeCount(likeRepository.countByPostId(post.getId()));
            dto.setCommentCount(commentRepository.countByPostId(post.getId()));

            dto.setViewCount(post.getViewCount());
            dto.setPostTime(post.getPostTime());
            return dto;
        });
    }
    public PostDetailDto getPost(Long postId) {
        log.info("Received request for getPost with postId: {}", postId);
        Optional<PostEntity> postOptional = postRepository.findPostWithUserAndSubCategoryById(postId);

        // Handle the case where no post is found
        PostEntity post = postOptional.orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));



        // SubCategory 정보 조회 (SubCategoryRepository 사용)
//        SubCategoryEntity subCategory = subCategoryRepository.findById(post.getSubCategoryId())
//                .orElseThrow(() -> new SubCategoryNotFoundException("SubCategory not found with id: " + post.getSubCategoryId()));

        List<CommentEntity> comments = commentRepository.findCommentsWithUserByPostId(postId);
        List<FileEntity> files = fileRepository.findByPostId(postId);
        long likeCount = likeRepository.countByPostId(postId);

        PostDetailDto postDetailDto = new PostDetailDto();
        postDetailDto.setId(post.getId());
        postDetailDto.setUserEmail(post.getUserEmail());
        postDetailDto.setProvider(post.getProvider());
        postDetailDto.setSubCategoryId(post.getSubCategoryId());
        postDetailDto.setTitle(post.getTitle());
        postDetailDto.setContent(post.getContent());
        postDetailDto.setPostTime(post.getPostTime());
        postDetailDto.setViewCount(post.getViewCount());
        Optional<UserEntity> userOptional = userRepository.findById(new UserId(post.getUserEmail(), post.getProvider()));
        userOptional.ifPresent(user -> postDetailDto.setNickname(user.getNickname()));
        Optional<SubCategoryEntity> subCategoryOptional = subCategoryRepository.findById(post.getSubCategoryId());
        subCategoryOptional.ifPresent(subCategory -> postDetailDto.setCategoryName(subCategory.getName()));
        postDetailDto.setLikeCount(likeRepository.countByPostId(post.getId()));
        postDetailDto.setCommentCount(commentRepository.countByPostId(post.getId()));

        postDetailDto.setComments(comments);
        postDetailDto.setFiles(files);
        log.info("Returning post: {}", postDetailDto);
        return postDetailDto;
    }


    @Transactional
    public PostEntity insertPost(PostEntity postEntity) {
        log.info("insertPost : " + postEntity);
        return postRepository.save(postEntity);
    }

    public PostEntity convertToEntity(PostDto postDto) {
        log.info("Converting : " + postDto);
        PostEntity postEntity = new PostEntity();
        postEntity.setTitle(postDto.getTitle());
        postEntity.setContent(postDto.getContent());
        postEntity.setViewCount(postDto.getViewCount());
        postEntity.setUserEmail(postDto.getUserEmail());
        postEntity.setProvider(postDto.getProvider());
        postEntity.setSubCategoryId(postDto.getSubCategoryId());
        return postEntity;
    }

    @Transactional
    public void saveFile(MultipartFile file, Long postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        String filePath = fileStorageService.storeFile(file);

        FileEntity postFileEntity = new FileEntity();
        postFileEntity.setPost(postEntity);
        postFileEntity.setFileUrl(filePath);
        fileRepository.save(postFileEntity);
    }


}
