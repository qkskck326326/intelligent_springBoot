package org.ict.intelligentclass.qna.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureDetailDto;
import org.ict.intelligentclass.qna.jpa.entity.QnaQuestionEntity;
import org.ict.intelligentclass.qna.jpa.entity.output.AnswerDto;
import org.ict.intelligentclass.qna.jpa.entity.output.QuestionDto;
import org.ict.intelligentclass.qna.model.service.QnaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 유저가 질문한 답변온 목록 보기
    @GetMapping("/aList/{nickname}")
    public Page<QuestionDto> getUserCheckQuestions(
            @PathVariable String nickname,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return qnaService.getUserCheckQuestions(nickname, pageable);
    }

    // 문의 상세 보기
    @GetMapping("/qList/detail/{questionId}")
    public ResponseEntity<QuestionDto> getDetailQuestion(@PathVariable("questionId") int questionId) {
        QuestionDto questionDto = qnaService.getQuestionDetailById(questionId);
        return new ResponseEntity<>(questionDto, HttpStatus.OK);
    }

    // 답변 상세 보기
    @GetMapping("/aList/detail/{questionId}")
    public ResponseEntity<AnswerDto> getDetailAnswer(@PathVariable("questionId") int questionId) {
        AnswerDto answerDto = qnaService.getAnswerDetailByQuestionId(questionId);
        return new ResponseEntity<>(answerDto, HttpStatus.OK);
    }

    // 문의 등록
    @PostMapping("/qList")
    public ResponseEntity<QuestionDto> insertQuestion(@RequestBody QuestionDto questionDto) {
        QuestionDto createdQuestion = qnaService.insertQuestion(questionDto);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    // 관리자 문의 목록 보기
    @GetMapping("/adminList")
    public ResponseEntity<Page<QuestionDto>> getAdminQuestionList(@PageableDefault(size = 10) Pageable pageable) {
        Page<QuestionDto> questionList = qnaService.getQuestionsByCheckStatus("N", pageable);
        return new ResponseEntity<>(questionList, HttpStatus.OK);
    }

    // 관리자 답변 등록 및 답변시 체크값 변환
    @PostMapping("/answer")
    public ResponseEntity<String> submitAnswer(@RequestBody AnswerDto answerDto) {
        qnaService.saveAnswer(answerDto);
        return new ResponseEntity<>("답변이 등록되었습니다.", HttpStatus.OK);
    }
}
