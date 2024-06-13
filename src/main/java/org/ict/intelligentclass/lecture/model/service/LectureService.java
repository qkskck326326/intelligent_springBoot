package org.ict.intelligentclass.lecture.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.RatingEntity;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureDetailDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureListDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.LecturePreviewDto;
import org.ict.intelligentclass.lecture.jpa.entity.output.PackageRatingDto;
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

    // 강의 목록 페이지
//    public List<LectureDto> selectAllLecture(Long lecturePackageId) {
//        List<LectureEntity> lectureEntities = lectureRepository.findByLecturePackageId(lecturePackageId);
//        List<LectureDto> lectureDtoList = new ArrayList<>();
//        for (LectureEntity lectureEntity : lectureEntities) {
//            lectureDtoList.add(lectureEntity.toDto());
//        }
//        return lectureDtoList;
//    } // 패키지 Id 로 강의 목록

    // 실험용 강의 목록 페이지
    public List<LectureListDto> selectAllLecture() {
        List<LectureEntity> lectureEntities = lectureRepository.findAll();
        List<LectureReadEntity> lectureReadEntities = lectureReadRepository.findAll();
        List<LectureListDto> lectureListDtos = new ArrayList<>();
        for (LectureEntity lectureEntity : lectureEntities) {
            // LectureReadEntity를 찾음
            Optional<LectureReadEntity> lectureReadEntity = lectureReadEntities.stream()
                    .filter(lr -> lr.getLectureId() == lectureEntity.getLectureId())
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

    public LecturePreviewDto getLecturePreviewById(int lectureId) {
        return lectureRepository.findById(lectureId)
                .map(LecturePreviewDto::new)
                .orElse(null);
    }

    // 강의 읽음 처리
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

    // 강의 수정
    // 강의 삭제
    // 강의 댓글 목록 가져오기
    // 강의 댓글 추가
    // 강의 댓글 수정
    // 강의 댓글 삭제
    // 강의 댓글 유저 정보 가져오기
    // 패키지 별점 가져오기
    // 별점 입력하기
    // 패키지 제목 가져오기

}










