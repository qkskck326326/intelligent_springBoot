package org.ict.intelligentclass.lecture.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.LectureCommentEntity;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.ict.intelligentclass.lecture.jpa.entity.input.CommentInput;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureDetailDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureListDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.LecturePreviewDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.PackageRatingDto;
import org.ict.intelligentclass.lecture.model.dto.LectureCommentDto;
import org.ict.intelligentclass.lecture.model.dto.LectureDto;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.lecture_packages.jpa.repository.LecturePackageRepository;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.ict.intelligentclass.lecture.jpa.entity.LectureReadEntity;
import org.ict.intelligentclass.lecture.jpa.repository.LectureCommentRepository;
import org.ict.intelligentclass.lecture.jpa.repository.LectureReadRepository;
import org.ict.intelligentclass.lecture.jpa.repository.LectureRepository;
import org.ict.intelligentclass.lecture.jpa.repository.RatingRepository;
import org.ict.intelligentclass.lecture.jpa.entity.input.RatingInput;
import org.ict.intelligentclass.lecture.jpa.entity.input.LectureInput;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LectureService {

    private final LecturePackageRepository lecturePackageRepository;
    private final LectureRepository lectureRepository;
    private final LectureCommentRepository lectureCommentRepository;
    private final LectureReadRepository lectureReadRepository;
    private final RatingRepository ratingRepository;

    // 강의 패키지 타이틀 가져오기
    public LecturePackageEntity selectLecturePackageTitle(Long lecturePackageId) {
        Optional<LecturePackageEntity> lecturePackageEntity = lecturePackageRepository.findById(lecturePackageId);
        return lecturePackageEntity.get();
    }

    // 패키지 Id 로 강의 목록 페이지
    public List<LectureListDto> selectAllLecture(Long lecturePackageId) {
        List<LectureEntity> lectureEntities = lectureRepository.findByLecturePackageId(lecturePackageId);
        List<LectureReadEntity> lectureReadEntities = lectureReadRepository.findAll();
        List<LectureListDto> lectureListDtos = new ArrayList<>();
        for (LectureEntity lectureEntity : lectureEntities) {
            Optional<LectureReadEntity> lectureReadEntity = lectureReadEntities.stream()
                    .filter(lr -> lr.getLectureId() == lectureEntity.getLectureId()) // 여기서 == 연산자를 사용합니다
                    .findFirst();
            lectureReadEntity.ifPresent(lr -> {
                LectureListDto lectureListDto = new LectureListDto(lectureEntity, lr);
                lectureListDtos.add(lectureListDto);
            });
        }
        return lectureListDtos;
    }


    // 강의 패키지 평균 별점 가져오기
    public PackageRatingDto selectLecturePackageRating(Long lecturePackageId) {
        Double averageRating = ratingRepository.findAverageRatingByLecturePackageId(lecturePackageId);
        PackageRatingDto ratingDto = new PackageRatingDto(lecturePackageId, averageRating.floatValue());
        return ratingDto;
    }

    // 강의 패키지 별점 입력
    public void addRating(RatingInput ratingInput) {
        RatingEntity ratingEntity = new RatingEntity();
        ratingEntity.setNickname(ratingInput.getNickname());
        ratingEntity.setLecturePackageId(ratingInput.getLecturePackageId());
        ratingEntity.setRating(ratingInput.getRating());
        ratingRepository.save(ratingEntity);
    }
    
    // 강의 미리보기
    public LecturePreviewDto getLecturePreviewById(int lectureId) {
        return lectureRepository.findById(lectureId)
                .map(LecturePreviewDto::new)
                .orElse(null);
    }

    // 강의 읽음 처리
    public void changeLectureRead(int lectureId, String nickname) {
        LectureReadEntity lectureReadEntity = lectureReadRepository.findByLectureIdAndNickname(lectureId, nickname)
                .orElseGet(() -> new LectureReadEntity(lectureId, nickname, "N"));

        lectureReadEntity.setLectureRead("Y");
        lectureReadRepository.save(lectureReadEntity);
    }

    // 강의 디테일 보기
    public LectureDetailDto getLectureDetailById(int lectureId) {
        return lectureRepository.findById(lectureId)
                .map(LectureDetailDto::new)
                .orElse(null);
    }

    // 강의 추가
    public void registerLecture(LectureInput lectureInput, Long lecturePackageId, String nickname) {
        LectureEntity lectureEntity = LectureEntity.builder()
                .lectureName(lectureInput.getLectureName())
                .lectureContent(lectureInput.getLectureContent())
                .lectureThumbnail(lectureInput.getLectureThumbnail())
                .streamUrl(lectureInput.getStreamUrl())
                .lecturePackageId(lecturePackageId)
                .nickname(nickname)
                .build();

        lectureRepository.save(lectureEntity);
    }

    // 강의 댓글 목록
    public List<LectureCommentDto> getLectureComments(int lectureId) {
        List<LectureCommentEntity> lectureCommentEntities = lectureCommentRepository.findByLectureId(lectureId);
        List<LectureCommentDto> lectureCommentDtos = new ArrayList<>();
        for (LectureCommentEntity lectureCommentEntity : lectureCommentEntities) {
            LectureCommentDto lectureCommentDto = LectureCommentDto.builder()
                    .lectureCommentId(lectureCommentEntity.getLectureCommentId())
                    .lectureId(lectureCommentEntity.getLectureId())
                    .lectureCommentReply(lectureCommentEntity.getLectureCommentReply())
                    .lectureCommentContent(lectureCommentEntity.getLectureCommentContent())
                    .lectureCommentDate(lectureCommentEntity.getLectureCommentDate())
                    .nickname(lectureCommentEntity.getNickname())
                    .parentCommentId(lectureCommentEntity.getParentCommentId())
                    .build();
            lectureCommentDtos.add(lectureCommentDto);
        }
        return lectureCommentDtos;
    }

    // 강의 댓글 추가
    public void insertLectureComment(CommentInput commentInput) {
        LectureCommentEntity lectureCommentEntity = new LectureCommentEntity();
        lectureCommentEntity.setLectureId(commentInput.getLectureId());
        lectureCommentEntity.setNickname(commentInput.getNickname());
        lectureCommentEntity.setLectureCommentContent(commentInput.getLectureCommentContent());
        lectureCommentEntity.setLectureCommentDate(new Date());
        lectureCommentEntity.setParentCommentId(commentInput.getParentCommentId());

        lectureCommentRepository.save(lectureCommentEntity);
    }

    // 강의 댓글 수정
    public void updateLectureComment(int lectureCommentId, String content) {
        Optional<LectureCommentEntity> lectureCommentEntityOpt = lectureCommentRepository.findByLectureCommentId(lectureCommentId);
        if (lectureCommentEntityOpt.isPresent()) {
            LectureCommentEntity lectureCommentEntity = lectureCommentEntityOpt.get();
            lectureCommentEntity.setLectureCommentContent(content);
            lectureCommentRepository.save(lectureCommentEntity);
        }
    }

    // 강의 댓글 삭제
    public void deleteLectureComment(int lectureCommentId) {
        lectureCommentRepository.deleteByLectureCommentId(lectureCommentId);
    }
    
    // 강의 수정
    // 강의 삭제
    // 강의 댓글 유저 정보 가져오기

}










