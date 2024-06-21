package org.ict.intelligentclass.payment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.model.service.LecturePackageService;
import org.ict.intelligentclass.payment.jpa.entity.CouponEntity;
import org.ict.intelligentclass.payment.model.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/payment")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin //리액트 애플리케이션(포트가 다름)의 자원 요청을 처리하기 위함
public class PaymentController {

    @Autowired
    private LecturePackageService lecturePackageService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/packages/{lecturePackageId}")
    public ResponseEntity<LecturePackageDetail> getPackage(@PathVariable Long lecturePackageId) {
        LecturePackageDetail lecturePackageDetail = lecturePackageService.getLecturePackageDetail(lecturePackageId);
        log.info("packageId : " + lecturePackageId);
        return ResponseEntity.ok(lecturePackageDetail);
    }

    @GetMapping("/coupons/{userEmail}")
    public List<CouponEntity> getCoupons(@PathVariable String userEmail){
        log.info("userEmail : " + userEmail); // 요청된 userEmail 로그 출력
        return paymentService.getCouponsByUserEmail(userEmail);
    }
}
