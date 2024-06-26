package org.ict.intelligentclass.qna.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.output.LecturePreviewDto;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    // 답변 O 목록
    public Page<QuestionDto> getUserCheckQuestions(String nickname, Pageable pageable) {
        Page<QnaQuestionEntity> questions = qnaQuestionRepository.findByNicknameAndQuestionCheck(nickname, "Y", pageable);
        return questions.map(QuestionDto::new);
    }

    // 질문 상세보기
    public QuestionDto getQuestionDetailById(int questionId) {
        return qnaQuestionRepository.findByQuestionId(questionId)
                .map(QuestionDto::new)
                .orElse(null);
    }

    // 답변 상세보기
    public AnswerDto getAnswerDetailByQuestionId(int questionId) {
        return qnaAnswerRepository.findByQuestionId(questionId)
                .map(AnswerDto::new)
                .orElse(null);
    }

    // 문의 등록
    public QuestionDto insertQuestion(QuestionDto questionDto) {
        QnaQuestionEntity questionEntity = QnaQuestionEntity.builder()
                .questionTitle(questionDto.getQuestionTitle())
                .questionContent(questionDto.getQuestionContent())
                .nickname(questionDto.getNickname())
                .questionDate(new Date())
                .questionCheck("N")
                .build();
        QnaQuestionEntity savedEntity = qnaQuestionRepository.save(questionEntity);
        return new QuestionDto(savedEntity);
    }

    // 관리자 문의 목록 보기
    public Page<QuestionDto> getQuestionsByCheckStatus(String checkStatus, Pageable pageable) {
        return qnaQuestionRepository.findByQuestionCheckOrderByQuestionDateAsc(checkStatus, pageable).map(QuestionDto::new);
    }

    // 관리자 답변 및 답변시 체크값 변환 및
    @Transactional
    public void saveAnswer(AnswerDto answerDto) {
        QnaAnswerEntity answer = new QnaAnswerEntity();
        answer.setQuestionId(answerDto.getQuestionId());
        answer.setAnswerContent(answerDto.getAnswerContent());
        answer.setNickname(answerDto.getNickname());
        answer.setAnswerDate(new Date());

        qnaAnswerRepository.save(answer);

        QnaQuestionEntity question = qnaQuestionRepository.findByQuestionId(answerDto.getQuestionId())
                .orElseThrow(() -> new RuntimeException("질문을 찾을 수 없습니다."));
        question.setQuestionCheck("Y");
        qnaQuestionRepository.save(question);
    }

}
