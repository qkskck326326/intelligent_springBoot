package org.ict.intelligentclass.post.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.post.jpa.entity.*;
import org.ict.intelligentclass.post.jpa.repository.TagRepository;
import org.ict.intelligentclass.post.model.dto.CommentDto;
import org.ict.intelligentclass.post.model.dto.LikeDto;
import org.ict.intelligentclass.post.model.dto.PostDetailDto;
import org.ict.intelligentclass.post.model.dto.PostDto;
import org.ict.intelligentclass.post.model.service.BookmarkService;
import org.ict.intelligentclass.post.model.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/posts")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin //리액트 애플리케이션(포트가 다름)의 자원 요청을 처리하기 위함
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    @Autowired

    private PostService postService;
    @Autowired
    private BookmarkService bookmarkService;

    @Autowired
    private TagRepository tagRepository;
// GET 요청부 ---------------------------------------------------------------------------------------------------------

    @GetMapping("/tags/popular")
    public ResponseEntity<List<String>> getTop10PopularTags() {
        List<String> popularTags = postService.getTop10PopularTags();
        return ResponseEntity.ok(popularTags);
    }

    // 내 댓글 가져오기
    // 내 댓글 가져오기
    @GetMapping("/comments/myComments")
    public ResponseEntity<List<CommentDto>> getMyComments(
            @RequestParam String userEmail,
            @RequestParam String provider,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentDto> comments = postService.getUserComments(userEmail, provider, pageable, sort);
        return ResponseEntity.ok(comments.getContent());
    }

    @PostMapping("/bookmark")
    public ResponseEntity<BookmarkEntity> addBookmark(@RequestBody
                                                          BookmarkEntity bookmarkEntity) {
        BookmarkEntity bookmark = bookmarkService.addBookmark(bookmarkEntity.getPostId(), bookmarkEntity.getUserEmail(), bookmarkEntity.getProvider());
        return ResponseEntity.ok(bookmark);
    }

    @DeleteMapping("/bookmark")
    public ResponseEntity<Void> removeBookmark(@RequestBody BookmarkEntity bookmarkEntity) {
        bookmarkService.removeBookmark(bookmarkEntity.getPostId(), bookmarkEntity.getUserEmail(), bookmarkEntity.getProvider());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myPosts")
    public Page<PostDto> getMyPosts(
            @RequestParam String userEmail,
            @RequestParam String provider,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<PostEntity> posts = postService.getUserPosts(userEmail, provider);
        List<PostDto> postDtos = posts.stream().
                map(postService::convertToDto).collect(Collectors.toList());
        return postService.applySorting(postDtos, pageable, sort);
    }

    @GetMapping("/bookmarks")
    public Page<PostDto> getBookmarks(
            @RequestParam String userEmail,
            @RequestParam String provider,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<PostEntity> posts = postService.getUserBookmarks(userEmail, provider);
        List<PostDto> postDtos = posts.stream().
                map(postService::convertToDto).collect(Collectors.toList());
        return postService.applySorting(postDtos, pageable, sort);
    }


    @GetMapping("/top5")
    public ResponseEntity<List<PostDto>> getTop5PopularPosts() {
        List<PostDto> topPosts = postService.getTop5PopularPosts();
        return ResponseEntity.ok(topPosts);
    }


    @GetMapping("/list")
    public Page<PostDto> getPosts(Pageable pageable, @RequestParam(defaultValue = "latest") String sort) {
        log.info("Received request for getPosts with pageable: {}", pageable);
        Page<PostDto> response = postService.getAllPosts(pageable, sort);
        log.info("Returning posts: {}", response.getContent());
        return response;
    }

    //게시물 검색 ------------------------------------------------------------

    @GetMapping("/searchByTag")
    public Page<PostDto> getSearchByTag(@RequestParam String tag, Pageable pageable,
                                        @RequestParam(defaultValue = "latest") String sort) {
        log.info("Received request for getSearchByTag with tag: {}", tag);
        Page<PostDto> response = postService.getSearchByTag(tag, pageable, sort);
        log.info("Returning posts: {}", response.getContent());
        return response;
    }

    @GetMapping("/searchTitleOrContent")
    public Page<PostDto> getSearchTitleOrContent(@RequestParam String keyword, Pageable pageable,
                                                 @RequestParam(defaultValue = "latest") String sort) {
        log.info("Received request for getSearchTitleOrContent with keyword: {}", keyword);
        Page<PostDto> response = postService.getSearchTitleOrContent(keyword, pageable, sort);
        log.info("Returning posts: {}", response.getContent());
        return response;
    }

    @GetMapping("/searchlistByCategory")
    public Page<PostDto> getSearchlistByCategory(@RequestParam Long categoryId,
                                                 Pageable pageable,
                                                 @RequestParam(defaultValue = "latest") String sort) {
        log.info("Received request for getSearchlistByCategory with categoryId: {} and sort: {}", categoryId, sort);
        Page<PostDto> response = postService.getSearchlistByCategory(categoryId, pageable, sort);
        log.info("Returning posts: {}", response.getContent());
        return response;
    }
    //클라이언트에서 받아온 값 쿠키에 삽입 전 값 인코더용 함수
    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    // 게시물 상세페이지 ------------------------------------------------------ -----------------------------------
    @GetMapping("/detail/{postId}")
    public ResponseEntity<PostDetailDto> getPost(@PathVariable Long postId, @RequestParam String userEmail,
                                                 @RequestParam String provider, @RequestParam String nickname, HttpServletRequest request,
                                                 HttpServletResponse response) {
        String encodedNickname = encodeValue(nickname);
        String viewCookieName = "postViewed_" + postId + "_" + encodedNickname;
        Cookie[] cookies = request.getCookies();
        boolean isViewed = false;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (viewCookieName.equals(cookie.getName())) {
                    isViewed = true;
                    break;
                }
            }
        } else {
        }
        if (!isViewed) {
            Cookie viewCookie = new Cookie(viewCookieName, "true");
            viewCookie.setPath("/");
            viewCookie.setMaxAge(60 * 60 * 24);
            viewCookie.setHttpOnly(false);
            response.addCookie(viewCookie);
        }

        PostDetailDto responseDto = postService.getPost(postId, userEmail, provider, isViewed);
        return ResponseEntity.ok(responseDto);
    }



    //POST 요청부 ----------------------------------------------------------------------------------------

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @RequestBody LikeEntity likeEntity) {
        logger.info("컨트롤러에서 확인용 Like : " + likeEntity.getUserEmail());
        System.out.println("라이크엔티티확인용" + likeEntity.getUserEmail());
        postService.likePost(postId, likeEntity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/unlike")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId, @RequestBody LikeEntity likeEntity) {
        postService.unlikePost(postId, likeEntity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Void> addComment(
            @PathVariable Long postId, @RequestBody CommentEntity commentEntity) {
        postService.addComment(postId, commentEntity);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{postId}/files")
    public ResponseEntity<Void> uploadFiles(@PathVariable Long postId,
                                            @RequestParam("files")
                                            List<MultipartFile> files) {
        for (MultipartFile file : files) {
            // 파일 저장 로직
            postService.saveFile(file, postId);
        }
        return ResponseEntity.ok().build();
    }
    //게시물 생성 ----------------------------------------------------------------------------------
    @PostMapping("/insert")
    public ResponseEntity<PostEntity> insertPost(@Valid @RequestBody PostDto postDto){
        log.info("Insert post", postDto);
        System.out.print(postDto);
        PostEntity postEntity = postService.convertToEntity(postDto);
        PostEntity savedPost = postService.insertPost(postEntity, postDto.getTags());
        return ResponseEntity.ok(savedPost);
    }

    //PUT 요청부 ---------------------------------------------------------------------------------------------
    @PutMapping("/update/{postId}")
    public ResponseEntity<PostEntity> updatePost(@PathVariable Long postId,
                                                 @Valid @RequestBody PostDto postDto) {
        log.info("Update post with id: {}", postId);
        PostEntity postEntity = postService.convertToEntity(postDto);
        postEntity.setId(postId); // postId를 엔티티에 설정
        PostEntity updatedPost = postService.updatePost(postEntity, postDto.getTags());
        return ResponseEntity.ok(updatedPost);
    }

    @PutMapping("/{postId}/UpdateComment/{commentId}")
    public ResponseEntity<Void> updateComment(
            @PathVariable Long postId, @PathVariable Long commentId,
            @RequestBody CommentEntity commentEntity) {
        commentEntity.setPostId(postId);
        commentEntity.setId(commentId);
        postService.upDateComment(commentEntity);
        return ResponseEntity.ok().build();
    }




    //DELETE 요청부 --------------------------------------------------------------------------------------------

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}/deleteComment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setPostId(postId);
        commentEntity.setId(commentId);
        postService.deleteComment(commentEntity);
        return ResponseEntity.ok().build();
    }

}
