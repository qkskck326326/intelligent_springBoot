package org.ict.intelligentclass.qna.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.qna.jpa.entity.QnaAnswerEntity;
import org.ict.intelligentclass.qna.jpa.entity.QnaQuestionEntity;
import org.ict.intelligentclass.qna.jpa.entity.output.AnswerDto;
import org.ict.intelligentclass.qna.jpa.entity.output.QuestionDto;
import org.ict.intelligentclass.qna.jpa.repository.QnaAnswerRepository;
import org.ict.intelligentclass.qna.jpa.repository.QnaQuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class QnaService {
    private final QnaAnswerRepository qnaAnswerRepository;
    private final QnaQuestionRepository qnaQuestionRepository;

    // 답변 X 목록
    public Page<QuestionDto> getUserQuestions(String nickname, Pageable pageable) {
        Page<QnaQuestionEntity> questions = qnaQuestionRepository.findByNicknameAndQuestionCheck(nickname, "N", pageable);
        return questions.map(QuestionDto::new);
    }

//    // 답변 O 목록
//    public List<QuestionDto> getQnaAnswerList(String nickname) {
//        List<QnaQuestionEntity> questions = qnaQuestionRepository.findByNicknameAndQuestionCheck(nickname, "Y");
//        return questions.stream()
//                .map(question -> new QuestionDto(
//                        question.getQuestionId(),
//                        question.getQuestionTitle(),
//                        question.getQuestionContent(),
//                        question.getNickname(),
//                        question.getQuestionDate(),
//                        question.getQuestionCheck()
//                ))
//                .collect(Collectors.toList());
//    }

    // 질문 상세보기

    // 답변 상세보기
    public List<AnswerDto> getQnaAnswerList(int questionId) {
        List<QnaAnswerEntity> answers = qnaAnswerRepository.findByQuestionId(questionId);
        return answers.stream()
                .map(answer -> new AnswerDto(
                        answer.getAnswerId(),
                        answer.getQuestionId(),
                        answer.getAnswerContent(),
                        answer.getNickname(),
                        answer.getAnswerDate()
                ))
                .collect(Collectors.toList());
    }

    // 문의 등록
    // 관리자 문의 목록 보기
    // 관리자 답변 등록
}
