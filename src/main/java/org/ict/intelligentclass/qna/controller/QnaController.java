package org.ict.intelligentclass.qna.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.qna.jpa.entity.output.QuestionDto;
import org.ict.intelligentclass.qna.model.service.QnaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/qna")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class QnaController {

    private final QnaService qnaService;

    // 유저가 질문한 문의 목록 보기
    @GetMapping("/qList/{nickname}")
    public Page<QuestionDto> getUserQuestions(
            @PathVariable String nickname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return qnaService.getUserQuestions(nickname, pageable);
    }


//    // 유저가 질문한 답변온 목록 보기
//    @GetMapping("/aList/{nickname}")
//    public Page<QuestionDto> getQnaAnswerList(@PathVariable String nickname, @RequestParam int page) {
//        return qnaService.getQnaAnswerList(nickname, PageRequest.of(page, 10));
//    }

    // 문의 상세 보기

    // 답변 상세 보기

    // 문의 등록
//    @PostMapping("/qList")
//    public ResponseEntity<String> insertQnaQuestion() {
//
//    }
//
//    // 관리자 문의 목록 보기
//    @GetMapping("/qnaList")
//    public List<QuestionDto> getAdminQuestionList() {
//
//    }
//
//    // 관리자 답변 등록
//    @PostMapping("/qnaList")
//    public ResponseEntity<String> insertAdminQuestion() {
//
//    }
}
