package org.ict.intelligentclass.post.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.post.model.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/posts")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin //리액트 애플리케이션(포트가 다름)의 자원 요청을 처리하기 위함
public class PostController {

    @Autowired
    private PostService postService;

//    @GetMapping("/list")
//    public Page<PostDto> getAllPosts(@RequestParam(defaultValue = "0") int page,
//                                     @RequestParam(defaultValue = "10") int size,
//                                     @RequestParam(defaultValue = "postTime") String sortBy){
//     return postService.getAllPosts(page, size, sortBy);
//    }

}
