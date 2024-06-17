package org.ict.intelligentclass.post.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.post.jpa.entity.CommentEntity;
import org.ict.intelligentclass.post.jpa.entity.LikeEntity;
import org.ict.intelligentclass.post.jpa.entity.PostEntity;
import org.ict.intelligentclass.post.model.dto.LikeDto;
import org.ict.intelligentclass.post.model.dto.PostDetailDto;
import org.ict.intelligentclass.post.model.dto.PostDto;
import org.ict.intelligentclass.post.model.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@RequestMapping("/posts")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin //리액트 애플리케이션(포트가 다름)의 자원 요청을 처리하기 위함
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostService.class);
    @Autowired
    private PostService postService;
// 게시물 리스트 출력 -----------------------------------------------------------------------------------------
    @GetMapping("/list")
    public Page<PostDto> getPosts(Pageable pageable) {
        log.info("Received request for getPosts with pageable: {}", pageable);
        Page<PostDto> response = postService.getAllPosts(pageable);
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
                logger.info("Existing Cookie Name: " + cookie.getName() + ", Value: " + cookie.getValue());
                if (viewCookieName.equals(cookie.getName())) {
                    isViewed = true;
                    break;
                }
            }
        } else {
            log.info("No cookies found in request");
        }

        if (!isViewed) {
            Cookie viewCookie = new Cookie(viewCookieName, "true");
            viewCookie.setPath("/");
            viewCookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간을 1일로 설정
            viewCookie.setHttpOnly(false); // 클라이언트 측에서 쿠키를 접근할 수 있도록 설정
            response.addCookie(viewCookie);
            log.info("Setting cookie: " + viewCookieName);
        }




        // 로그 추가
        System.out.println("isViewed 확인 : " + isViewed);
        log.info("Received request for getPost with postId: {}", postId);
        PostDetailDto responseDto = postService.getPost(postId, userEmail, provider, isViewed);
        log.info("Returning post: {}", responseDto);
        return ResponseEntity.ok(responseDto);
    }

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
    public ResponseEntity<Void> addComment(@PathVariable Long postId, @RequestBody CommentEntity commentEntity) {
        postService.addComment(postId, commentEntity);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{postId}/files")
    public ResponseEntity<Void> uploadFiles(@PathVariable Long postId, @RequestParam("files") List<MultipartFile> files) {
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
        PostEntity savedPost = postService.insertPost(postEntity);
        return ResponseEntity.ok(savedPost);
    }

}
