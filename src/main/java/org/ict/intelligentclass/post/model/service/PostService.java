package org.ict.intelligentclass.post.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.exception.PostNotFoundException;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.lecture_packages.jpa.repository.SubCategoryRepository;
import org.ict.intelligentclass.post.jpa.entity.CommentEntity;
import org.ict.intelligentclass.post.jpa.entity.FileEntity;
import org.ict.intelligentclass.post.jpa.entity.LikeEntity;
import org.ict.intelligentclass.post.jpa.entity.PostEntity;


import org.ict.intelligentclass.post.jpa.repository.*;
import org.ict.intelligentclass.post.model.dto.CommentDto;
import org.ict.intelligentclass.post.model.dto.ContentBlock;
import org.ict.intelligentclass.post.model.dto.PostDetailDto;
import org.ict.intelligentclass.post.model.dto.PostDto;
import org.ict.intelligentclass.post.storage.FileStorageService;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.jpa.entity.id.UserId;
import org.ict.intelligentclass.user.jpa.repository.UserRepository;
import org.ict.intelligentclass.user.model.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

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

    // 인기 게시물 가져오기 메서드
    public List<PostDto> getTop5PopularPosts() {
        List<PostEntity> posts = postRepository.findAll();

        return posts.stream()
                .map(this::convertToDto)
                .sorted((post1, post2) -> Long.compare(calculateScore(post2), calculateScore(post1)))
                .limit(10)
                .collect(Collectors.toList());
    }

    private long calculateScore(PostDto post) {
        long viewWeight = 1;
        long likeWeight = 2;
        long commentWeight = 3;

        return post.getViewCount() * viewWeight +
                post.getLikeCount() * likeWeight +
                post.getCommentCount() * commentWeight;
    }

    public Page<PostDto> getAllPosts(Pageable pageable, String sort) {
        log.info("Fetching all posts with pageable: {} and sort: {}", pageable, sort);
        List<PostEntity> posts = postRepository.findAll();
        List<PostDto> postDtos = posts.stream().map(this::convertToDto).collect(Collectors.toList());
        return applySorting(postDtos, pageable, sort);
    }

    public Page<PostDto> getSearchTitleOrContent(String keyword, Pageable pageable, String sort) {
        log.info("Fetching posts with keyword: {} and pageable: {} and sort: {}", keyword, pageable, sort);
        List<PostEntity> posts = postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        List<PostDto> postDtos = posts.stream().map(this::convertToDto).collect(Collectors.toList());
        return applySorting(postDtos, pageable, sort);
    }

    public Page<PostDto> getSearchlistByCategory(Long categoryId, Pageable pageable, String sort) {
        log.info("Fetching posts by categoryId: {} and pageable: {} and sort: {}", categoryId, pageable, sort);
        List<PostEntity> posts = postRepository.findBySubCategoryId(categoryId);
        List<PostDto> postDtos = posts.stream().map(this::convertToDto).collect(Collectors.toList());
        return applySorting(postDtos, pageable, sort);
    }

    private Page<PostDto> applySorting(List<PostDto> postDtos, Pageable pageable, String sort) {
        Comparator<PostDto> comparator;
        switch (sort) {
            case "likes":
                comparator = Comparator.comparingLong(PostDto::getLikeCount).reversed();
                break;
            case "comments":
                comparator = Comparator.comparingLong(PostDto::getCommentCount).reversed();
                break;
            case "views":
                comparator = Comparator.comparingLong(PostDto::getViewCount).reversed();
                break;
            case "latest":
            default:
                comparator = Comparator.comparing(PostDto::getPostTime).reversed();
                break;
        }
        postDtos.sort(comparator);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), postDtos.size());
        List<PostDto> sortedPosts = postDtos.subList(start, end);
        return new PageImpl<>(sortedPosts, pageable, postDtos.size());
    }

    private PostDto convertToDto(PostEntity post) {
        Optional<UserEntity> userOptional = userRepository.findById(new UserId(post.getUserEmail(), post.getProvider()));
        UserDto userDto = userOptional.map(UserEntity::toDto).orElse(null);

        Optional<SubCategoryEntity> subCategoryOptional = subCategoryRepository.findById(post.getSubCategoryId());
        String categoryName = subCategoryOptional.map(SubCategoryEntity::getName).orElse("");

        long likeCount = likeRepository.countByPostId(post.getId());
        long commentCount = commentRepository.countByPostId(post.getId());

        return post.toDto(userDto, categoryName, (int) likeCount, (int) commentCount);
    }



    public PostDetailDto getPost(Long postId, String userEmail, String provider, boolean isViewed) {
        log.info("Received request for getPost with postId: {}", postId);
        log.info("isViewed 확인 : " + isViewed);
        Optional<PostEntity> postOptional = postRepository.findPostWithUserAndSubCategoryById(postId);
        PostEntity post = postOptional.orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));
        System.out.println("Post ID: " + postId + ", User: " + userEmail + ", Provider: " + provider);
        System.out.println("isViewed: " + isViewed);
        if (!isViewed) {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
            System.out.println("조회수 증가: " + post.getViewCount());
        }

        List<CommentEntity> comments = commentRepository.findCommentsWithUserByPostId(postId);
        List<FileEntity> files = fileRepository.findByPostId(postId);
        long likeCount = likeRepository.countByPostId(postId);
        boolean userLiked = likeRepository.existsByPostIdAndUserEmailAndProvider(postId, userEmail, provider);

        Optional<UserEntity> userOptional = userRepository.findById(new UserId(post.getUserEmail(), post.getProvider()));
        UserDto userDto = userOptional.map(UserEntity::toDto).orElse(null);

        Optional<SubCategoryEntity> subCategoryOptional = subCategoryRepository.findById(post.getSubCategoryId());
        String categoryName = subCategoryOptional.map(SubCategoryEntity::getName).orElse("");

        List<CommentDto> commentDto = comments.stream()
                .map(comment -> {
                    Optional<UserEntity> commentUserOptional = userRepository.findById(new UserId(comment.getUserEmail(), comment.getProvider()));
                    UserDto commentUserDto = commentUserOptional.map(UserEntity::toDto).orElse(null);
                    return comment.toDto(commentUserDto);
                })
                .collect(Collectors.toList());

        PostDetailDto postDetailDto = post.toDetailDto(userDto, categoryName, userLiked, likeCount, comments.size(), commentDto, files);

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

    public void saveFile(MultipartFile file, Long postId) {
        logger.info("Saving file for post ID: {}", postId);
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        logger.error("Post not found with ID: {}", postId);
        String fileName = fileStorageService.storeFile(file);
        String fileUrl = "/api/files/" + fileName;
        logger.info("Generated file URL: {}", fileUrl);
        FileEntity postFileEntity = new FileEntity();
        postFileEntity.setPost(postEntity);
        postFileEntity.setFileUrl(fileUrl);
        postFileEntity.setUploadTime(new Timestamp(System.currentTimeMillis()));
        fileRepository.save(postFileEntity);
        logger.info("File saved with URL: {}", fileUrl);
    }


    public void likePost(Long postId, LikeEntity likeEntity) {
        logger.info("Like : " + likeEntity.getUserEmail());
        likeEntity.setPostId(postId);
        likeRepository.save(likeEntity);
    }

    public void unlikePost(Long postId, LikeEntity likeEntity) {
        LikeEntity existingLike = likeRepository.findByPostIdAndUserEmailAndProvider(postId, likeEntity.getUserEmail(), likeEntity.getProvider());
        if (existingLike != null) {
            likeRepository.delete(existingLike);
        }
    }

    public void addComment(Long postId, CommentEntity commentEntity) {
        commentEntity.setPostId(postId);
        commentRepository.save(commentEntity);
    }



    public PostEntity updatePost(PostEntity postEntity) {
        if (postEntity.getPostTime() == null) {
            postEntity.setPostTime(LocalDateTime.now());
        }
        return postRepository.save(postEntity);
    }


    public void upDateComment(CommentEntity commentEntity) {
        if (commentEntity.getCommentTime() == null) {
            commentEntity.setCommentTime(LocalDateTime.now());
        }
        commentRepository.save(commentEntity);
    }

    @Transactional
    public void deletePost(Long postId) {
        commentRepository.deleteByPostId(postId);
        fileRepository.deleteByPostId(postId);
        likeRepository.deleteByPostId(postId);
        postRepository.deleteById(postId);
    }

    public void deleteComment(CommentEntity commentEntity) {
        commentRepository.delete(commentEntity);
    }

}
