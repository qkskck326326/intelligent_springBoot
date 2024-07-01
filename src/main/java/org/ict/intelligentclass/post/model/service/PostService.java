package org.ict.intelligentclass.post.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.exception.PostNotFoundException;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.lecture_packages.jpa.repository.SubCategoryRepository;
import org.ict.intelligentclass.post.jpa.entity.*;


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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
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

    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostTagRepository postTagRepository;

    public List<String> getTop10PopularTags() {
        return tagRepository.findTop10PopularTags();
    }

    // 북마크 추가
    public BookmarkEntity addBookmark(Long postId, String userEmail, String provider) {
        return bookmarkService.addBookmark(postId, userEmail, provider);
    }

    // 북마크 제거
    public void removeBookmark(Long postId, String userEmail, String provider) {
        bookmarkService.removeBookmark(postId, userEmail, provider);
    }

    // 사용자의 북마크 목록 가져오기
    public List<PostEntity> getUserBookmarks(String userEmail, String provider) {
        List<BookmarkEntity> bookmarks = bookmarkService.getUserBookmarks(userEmail, provider);
        return bookmarks.stream()
                .map(bookmark -> postRepository.findById(bookmark.getPostId()).orElse(null))
                .collect(Collectors.toList());
    }

    // 게시물이 북마크되었는지 확인
    public boolean isPostBookmarked(Long postId, String userEmail, String provider) {
        return bookmarkService.isPostBookmarked(postId, userEmail, provider);
    }

    // 사용자의 게시글 목록 가져오기
    public List<PostEntity> getUserPosts(String userEmail, String provider) {
        return postRepository.findByUserEmailAndProvider(userEmail, provider);
    }

    // 인기 게시물 가져오기 메서드
    public List<PostDto> getTop5PopularPosts() {

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);

//        List<PostEntity> posts = postRepository.findAll();
        // 1주일 전부터 현재까지의 게시물을 가져옵니다.
        List<PostEntity> posts = postRepository.findPopularPostsSince(oneWeekAgo);

        return posts.stream()
                .map(this::convertToDto)
                .sorted((post1, post2) -> Long.compare(calculateScore(post2), calculateScore(post1)))
                .limit(5)
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

    public Page<PostDto> applySorting(List<PostDto> postDtos, Pageable pageable, String sort) {
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

    public PostDto convertToDto(PostEntity post) {
        Optional<UserEntity> userOptional = userRepository.findById(new UserId(post.getUserEmail(), post.getProvider()));
        UserDto userDto = userOptional.map(UserEntity::toDto).orElse(null);

        Optional<SubCategoryEntity> subCategoryOptional = subCategoryRepository.findById(post.getSubCategoryId());
        String categoryName = subCategoryOptional.map(SubCategoryEntity::getName).orElse("");

        long likeCount = likeRepository.countByPostId(post.getId());
        long commentCount = commentRepository.countByPostId(post.getId());
        List<String> tags = postTagRepository.findByIdPostId(post.getId())
                .stream()
                .map(postTagEntity -> postTagEntity.getTag().getName())
                .collect(Collectors.toList());
        PostDto postDto = post.toDto(userDto, categoryName, (int) likeCount, (int) commentCount, tags);
        if (postDto.getPostTime() == null) {
            postDto.setPostTime(LocalDateTime.now());
        }

        return postDto;
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
        boolean userBookmarked = bookmarkService.isPostBookmarked(postId, userEmail, provider); // 북마크 상태 확인

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
        List<String> tags = postTagRepository.findByIdPostId(postId)
                .stream()
                .map(postTagEntity -> postTagEntity.getTag().getName())
                .collect(Collectors.toList());

        PostDetailDto postDetailDto = post.toDetailDto(userDto, categoryName, userLiked, likeCount, comments.size(), commentDto, files, tags);
        postDetailDto.setUserBookmarked(userBookmarked); // 북마크 상태 설정
        log.info("Returning post: {}", postDetailDto);
        return postDetailDto;
    }


    @Transactional
    public PostEntity insertPost(PostEntity postEntity, List<String> tagNames) {
        PostEntity savedPost = postRepository.save(postEntity);
        log.info("insertPost : " + postEntity);
        for (String tagName : tagNames) {
            TagEntity tagEntity = tagRepository.findByName(tagName);
            if (tagEntity == null) {
                tagEntity = new TagEntity();
                tagEntity.setName(tagName);
                tagEntity = tagRepository.save(tagEntity);
            }
            PostTagEntity postTagEntity = new PostTagEntity();
            postTagEntity.setId(new PostTagId(savedPost.getId(), tagEntity.getId()));
            postTagEntity.setPost(savedPost);
            postTagEntity.setTag(tagEntity);
            postTagRepository.save(postTagEntity);
        }
        return savedPost;
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



    @Transactional
    public PostEntity updatePost(PostEntity postEntity, List<String> tagNames) {
        if (postEntity.getPostTime() == null) {
            postEntity.setPostTime(LocalDateTime.now());}
        PostEntity updatedPost = postRepository.save(postEntity);

        // 기존 태그 삭제
        postTagRepository.deleteByPostId(updatedPost.getId());


          for (String tagName : tagNames) {
            TagEntity tagEntity = tagRepository.findByName(tagName);
            if (tagEntity == null) {
                tagEntity = new TagEntity();
                tagEntity.setName(tagName);
                tagEntity = tagRepository.save(tagEntity);
            }
            PostTagEntity postTagEntity = new PostTagEntity();
            postTagEntity.setId(new PostTagId(updatedPost.getId(), tagEntity.getId()));
            postTagEntity.setPost(updatedPost);
            postTagEntity.setTag(tagEntity);
            postTagRepository.save(postTagEntity);
        }
        return updatedPost;
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

    public Page<PostDto> getSearchByTag(String tag, Pageable pageable, String sort) {
        log.info("Fetching posts with tag: {} and pageable: {} and sort: {}", tag, pageable, sort);
        List<PostEntity> posts = postRepository.findByTagName(tag);
        List<PostDto> postDtos = posts.stream().map(this::convertToDto).collect(Collectors.toList());
        return applySorting(postDtos, pageable, sort);
    }

    public Page<CommentDto> getUserComments(String userEmail, String provider, Pageable pageable, String sort) {
        List<CommentEntity> comments = commentRepository.findByUserEmailAndProvider(userEmail, provider);
        List<CommentDto> commentDtos = comments.stream()
                .map(comment -> {
                    Optional<UserEntity> commentUserOptional = userRepository.findById(new UserId(comment.getUserEmail(), comment.getProvider()));
                    UserDto commentUserDto = commentUserOptional.map(UserEntity::toDto).orElse(null);
                    return comment.toDto(commentUserDto);
                })
                .collect(Collectors.toList());
        return applyCommentSorting(commentDtos, pageable, sort);
    }

    // 댓글 정렬 적용
    private Page<CommentDto> applyCommentSorting(List<CommentDto> commentDtos, Pageable pageable, String sort) {
        Comparator<CommentDto> comparator;
        switch (sort) {
            case "latest":
                comparator = Comparator.comparing(CommentDto::getCommentTime).reversed();
                break;
            default:
                comparator = Comparator.comparing(CommentDto::getCommentTime).reversed();
                break;
        }
        commentDtos.sort(comparator);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), commentDtos.size());
        List<CommentDto> sortedComments = commentDtos.subList(start, end);
        return new PageImpl<>(sortedComments, pageable, commentDtos.size());
    }
}
