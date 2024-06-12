package org.ict.intelligentclass.post.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.entity.SubCategoryEntity;
import org.ict.intelligentclass.post.jpa.entity.PostEntity;
import org.ict.intelligentclass.post.model.dto.PostDetailDto;
import org.ict.intelligentclass.post.model.dto.PostDto;
import org.ict.intelligentclass.post.model.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/posts")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin //리액트 애플리케이션(포트가 다름)의 자원 요청을 처리하기 위함
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public Page<PostDto> getPosts(Pageable pageable) {
        log.info("Received request for getPosts with pageable: {}", pageable);
        Page<PostDto> response = postService.getAllPosts(pageable);
        log.info("Returning posts: {}", response.getContent());
        return response;
    }

    @GetMapping("/detail/{postId}")
    public ResponseEntity<PostDetailDto> getPost(@PathVariable Long postId) {
        log.info("Received request for getPost with postId: {}", postId);
        PostDetailDto response = postService.getPost(postId);
        log.info("Returning post: {}", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/insert")
    public ResponseEntity<PostEntity> insertPost(@Valid @RequestBody PostDto postDto){
        log.info("Insert post", postDto);
        System.out.print(postDto);
        PostEntity postEntity = postService.convertToEntity(postDto);
        PostEntity savedPost = postService.insertPost(postEntity);
        return ResponseEntity.ok(savedPost);
    }

    @PostMapping("/{postId}/files")
    public ResponseEntity<Void> uploadFile(@PathVariable Long postId, @RequestParam("file") MultipartFile file) {
        postService.saveFile(file, postId);
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/images/{filename}")
//    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
//        Resource resource = new ClassPathResource("static/" + filename);
//        return ResponseEntity.ok()
//                .contentType(MediaType.IMAGE_JPEG)
//                .body(resource);
//    }

//    @GetMapping("/list")
//    public Page<PostDto> getAllPosts(@RequestParam(defaultValue = "0") int page,
//                                     @RequestParam(defaultValue = "10") int size,
//                                     @RequestParam(defaultValue = "postTime") String sortBy){
//     return postService.getAllPosts(page, size, sortBy);
//    }

}
