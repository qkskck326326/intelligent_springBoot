package org.ict.intelligentclass.lecture_packages.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ict.intelligentclass.lecture_packages.jpa.entity.LecturePackageEntity;

import org.ict.intelligentclass.lecture_packages.jpa.input.LecturePackageRegister;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageList;

import org.ict.intelligentclass.lecture_packages.model.service.LecturePackageService;
import org.ict.intelligentclass.user.model.dto.UserDto;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/packages")
public class LecturePackageController {

    private final LecturePackageService lecturePackageService;
    private final UserService userService;



    //패키지리스트 조회
//    @GetMapping
//    public ResponseEntity<Page<LecturePackageList>> getLecturePackages(
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam(required = false) String sortCriteria,
//            @RequestParam(required = false) String searchTerm,
//            @RequestParam(required = false) Long subCategoryId,
//            @RequestParam(required = false) String searchCriteria) {
//        Page<LecturePackageList> lecturePackages = lecturePackageService.getAllLecturePackages(page, size, sortCriteria, searchTerm, subCategoryId, searchCriteria);
//        return ResponseEntity.ok(lecturePackages);
//    }




//    @GetMapping  이거야ㅑㅑㅑ
//    public ResponseEntity<Page<LecturePackageList>> getLecturePackages(
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam(required = false) String sortCriteria,
//            @RequestParam(required = false) String searchTerm,
//            @RequestParam(required = false) Long subCategoryId,
//            @RequestParam(required = false) String searchCriteria) {
//
//        Page<LecturePackageList> lecturePackages;
//
//        if (subCategoryId != null && sortCriteria != null) {
//            lecturePackages = lecturePackageService.getLecturePackagesBySubCategory(page, size, sortCriteria, subCategoryId);
//        } else if (searchTerm != null && searchCriteria != null && sortCriteria != null) {
//            lecturePackages = lecturePackageService.getLecturePackagesBySearch(page, size, sortCriteria, searchTerm, searchCriteria);
//        } else {
//            lecturePackages = lecturePackageService.getAllLecturePackages(page, size, sortCriteria);
//        }
//
//        return ResponseEntity.ok(lecturePackages);
//    }





    @GetMapping
    public Page<LecturePackageList> getLecturePackages(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String sortCriteria,
            @RequestParam(required = false) String searchCriteria,
            @RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) Long subCategoryId) {
        return lecturePackageService.getLecturePackages(page, size, sortCriteria, searchCriteria, searchTerm, subCategoryId);
    }






//    // 카테고리로 필터링된 패키지 리스트 조회
//    @GetMapping("/categorysort")
//    public ResponseEntity<List<LecturePackageList>> getCategorySortPackages(@RequestParam Long categoryId) {
//        List<LecturePackageList> lecturePackageLists = lecturePackageService.getCategorySortedPackages(categoryId);
//        log.info("getCategorySortPackages : {}", lecturePackageLists);
//        return ResponseEntity.ok(lecturePackageLists);
//    }

    // 유저관심패키지 TOP10
    @GetMapping("/interestpackagetop10")
    public ResponseEntity<List<LecturePackageList>> getInterestPackages(@RequestParam String email, @RequestParam String provider) {
        List<LecturePackageList> lecturePackageLists = lecturePackageService.getInterestPackages(email, provider);

        log.info("email, provider : ", email, provider);
        return ResponseEntity.ok(lecturePackageLists);
    }

    // 모든 상위카테고리별 패키지 TOP4
    @GetMapping("/upperCategorypackageall")
    public ResponseEntity<Map<Long, List<LecturePackageList>>> getUpperCategoryPackageALl(){
        Map<Long, List<LecturePackageList>> lecturePackageLists = lecturePackageService.getUpperCategoryPackageAll();
        return ResponseEntity.ok(lecturePackageLists);
    }



    //패키지 상세보기
    @GetMapping("/detail")
    public ResponseEntity<LecturePackageDetail> getLecturePackageById(@RequestParam Long lecturePackageId) {
        log.info("getLecturePackageById : ", lecturePackageId);

        LecturePackageDetail lecturePackageDetail = lecturePackageService.getLecturePackageDetail(lecturePackageId);
        return ResponseEntity.ok(lecturePackageDetail);
    }

    //패키지 등록하기
    @PostMapping
    public ResponseEntity<LecturePackageEntity> createLecturePackage(@RequestBody LecturePackageRegister lecturePackageRegister) {
        log.info("createLecturePackage : ", lecturePackageRegister);

        LecturePackageEntity savedLecturePackage = lecturePackageService.createLecturePackage(lecturePackageRegister);
        return ResponseEntity.ok(savedLecturePackage);

    }
    //패키지 수정하기
    @PutMapping
    public ResponseEntity<LecturePackageEntity> updateLecturePackage(@RequestParam Long lecturePackageId,
                                                                     @RequestBody LecturePackageRegister lecturePackageRegister) {
        LecturePackageEntity modifyLecturePackage = lecturePackageService.modifyLecturePackage(lecturePackageId, lecturePackageRegister);
        return ResponseEntity.ok(modifyLecturePackage);

    }

    //패키지 삭제하기
    @DeleteMapping("/{lecturePackageId}")
    public ResponseEntity<Void> deleteLecturePackage(@PathVariable Long lecturePackageId) {
        lecturePackageService.deleteLecturePackage(lecturePackageId);
        return ResponseEntity.noContent().build();
    }



    //쿠키에 삽입전 인코더
    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }




    //패키지조회수 1 올리기
    @PutMapping("/view/{lecturePackageId}")
    public void increaseViewCount(@PathVariable Long lecturePackageId, @RequestParam String nickname, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("increaseViewCount called with lecturePackageId: " + lecturePackageId + " and nickname: " + nickname);

        String encodedNickname = encodeValue(nickname);
        String viewCookieName = "packageViewed_" + lecturePackageId + "_" + encodedNickname;

        Cookie[] cookies = request.getCookies();
        boolean isViewed = false;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (viewCookieName.equals(cookie.getName())) {
                    isViewed = true;
                    break;
                }
            }
        }

        String authorNickname = lecturePackageService.getAuthorNickname(lecturePackageId);
        System.out.println("Author nickname: " + authorNickname);

        if (nickname.equals(authorNickname)) {
            if (!isViewed) {
                Cookie viewCookie = new Cookie(viewCookieName, "true");
                viewCookie.setPath("/");
                viewCookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간을 1일로 설정
                viewCookie.setHttpOnly(false); // 클라이언트 측에서 쿠키를 접근할 수 있도록 설정
                response.addCookie(viewCookie);
                lecturePackageService.increaseViewCount(lecturePackageId);
                System.out.println("View count increased for author.");
            } else {
                System.out.println("Author has already viewed this today.");
            }
        } else {
            lecturePackageService.increaseViewCount(lecturePackageId);
            System.out.println("View count increased for non-author.");
        }
    }




    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserByNickname(@RequestParam String nickname){
        UserDto user = userService.getUserByNickname(nickname);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/lecturecount")
    public ResponseEntity<Integer> getLectureCount(@RequestParam Long lecturePackageId){
        int lectureCount = lecturePackageService.getLectureCount(lecturePackageId);
        return ResponseEntity.ok(lectureCount);
    }


}