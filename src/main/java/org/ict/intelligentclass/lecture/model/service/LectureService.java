package org.ict.intelligentclass.lecture.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture.jpa.entity.output.LectureListDto;
import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;
import org.ict.intelligentclass.lecture_packages.jpa.repository.LecturePackageRepository;
import org.ict.intelligentclass.lecture.jpa.entity.LectureEntity;
import org.ict.intelligentclass.lecture.jpa.entity.LectureReadEntity;
import org.ict.intelligentclass.lecture.jpa.repository.LectureCommentRepository;
import org.ict.intelligentclass.lecture.jpa.repository.LectureReadRepository;
import org.ict.intelligentclass.lecture.jpa.repository.LectureRepository;
import org.ict.intelligentclass.lecture.jpa.repository.RatingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    // 강의 읽음 가져오기
//    public String selectLectureRead(String nickname, int lectureId) {
//        LectureRead lectureRead = lectureReadRepository.findByNicknameAndLectureId(nickname, lectureId);
//
//
//    }


    // 강의 미리보기
//    public List<LecturePreviewDto> getLecturePreviewsByLectureId(int lectureId) {
//        return lectureRepository.getLecturePreviewsByLectureId(lectureId);
//    }

    // 강의 읽음 처리


    // 강의 디테일 보기
//    public LectureDto selectLectureDetail(int lectureId) {
//        Optional<LectureEntity> optionalLectureEntity = lectureRepository.findById(lectureId);
//        LectureEntity LectureEntity = optionalLectureEntity.get();
//
//        return LectureEntity.toDto();
//    }

    // 강의 추가
//    public void insertLecture(LectureDto LectureDto) {
//        LectureDto.setLectureId(lectureRepository.findLastLectureId() + 1);
//        lectureRepository.save(LectureDto.toEntity());
//    }

    // 강의 수정
//    public void updateLecture(LectureDto LectureDto) {
//        LectureEntity LectureEntity = lectureRepository.getReferenceById(LectureDto.getLectureId());
//        LectureEntity.setLectureName(LectureDto.getLectureName());
//        LectureEntity.setLectureContent(LectureDto.getLectureContent());
//        LectureEntity.setLectureThumbnail(LectureDto.getLectureThumbnail());
//        LectureEntity.setLectureVideo(LectureDto.getLectureVideo());
//
//        lectureRepository.save(LectureEntity);
//    }

    // 강의 삭제
//    public void deleteLecture(int lectureId) {
//        lectureRepository.deleteById(lectureId);
//    }

    // 강의 댓글 목록 가져오기
//    public List<LectureCommentDto> selectLectureComment(int lectureId) {
//        List<LectureCommentEntity> lectureCommentEntities = lectureCommentRepository.findAllById(lectureId);
//        List<LectureCommentDto> lectureCommentDtos = new ArrayList<>();
//        for (LectureCommentEntity lectureCommentEntity : lectureCommentEntities) {
//            lectureCommentDtos.add(lectureCommentEntity.toDto());
//        }
//        return lectureCommentDtos;
//    }

    // 강의 댓글 추가



    // 강의 댓글 수정
//    public void updateLectureComment(LectureCommentDto lectureCommentDto) {
//        LectureCommentEntity lectureCommentEntity = lectureCommentRepository.getReferenceById(lectureCommentDto.getLectureCommentId());
//        lectureCommentEntity.setLectureCommentContent(lectureCommentDto.getLectureCommentContent());
//        lectureCommentRepository.save(lectureCommentEntity);
//    }

    // 강의 댓글 삭제
//    public void deleteLectureComment(int lectureCommentId) {
//        lectureCommentRepository.deleteById(lectureCommentId);
//    }

    // 강의 댓글 유저 정보 가져오기
//    public LectureCommentDto selectLectureCommentUser(int lectureCommentId) {
//        Optional<LectureCommentEntity> optionalLectureCommentEntity = lectureCommentRepository.findById(lectureCommentId);
//        LectureCommentEntity lectureCommentEntity = optionalLectureCommentEntity.get();
//
//        return lectureCommentEntity.toDto();
//    }

    // 패키지 별점 가져오기
    // 별점 입력하기
    // 패키지 제목 가져오기

}










