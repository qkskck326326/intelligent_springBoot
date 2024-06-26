package org.ict.intelligentclass.payment.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.model.service.LecturePackageService;
import org.ict.intelligentclass.payment.jpa.entity.CouponEntity;
import org.ict.intelligentclass.payment.jpa.entity.PaymentEntity;
import org.ict.intelligentclass.payment.model.dto.PaymentRequest;
import org.ict.intelligentclass.payment.model.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<CouponEntity> getCoupons(@PathVariable String userEmail) {
        log.info("userEmail : " + userEmail); // 요청된 userEmail 로그 출력
        return paymentService.getCouponsByUserEmail(userEmail);
    }

    @PostMapping("/savePayment")
    public ResponseEntity<String> savePayment(@RequestBody PaymentEntity paymentEntity) {
        log.info("lecturePackageDetail : " + paymentEntity);
        paymentService.savePayment(paymentEntity);
        log.info("Transaction history saved: {}", paymentEntity);
        return ResponseEntity.ok("결제 완료");
    }

    @PostMapping("/request")
    public ResponseEntity<?> savePaymentRequest(@RequestBody Map<String, Object> paymentRequest, HttpSession session) {
        session.setAttribute("paymentRequest", paymentRequest);
        log.info("결제정보저장 확인 : {}", paymentRequest);
        log.info("결제저장 세션에서 확인: {}", session.getAttribute("paymentRequest"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/session-info")
    public ResponseEntity<?> getSessionInfo(HttpSession session) {
        Map<String, Object> paymentRequest = (Map<String, Object>) session.getAttribute("paymentRequest");
        if (paymentRequest == null) {
            return ResponseEntity.badRequest().body("No payment information in session.");
        }
        log.info("결제정보 가져오기 확인: {}", paymentRequest);
        log.info("결제정보 세션에서 가져오기: {}", session.getAttribute("paymentRequest"));
        return ResponseEntity.ok(paymentRequest);
    }

    @PostMapping("/clear-session")
    public ResponseEntity<?> clearSession(HttpSession session) {
        session.removeAttribute("paymentRequest");
        log.info("결제정보 세션에서 삭제");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity<?> deleteCoupon(@PathVariable Long couponId) {
        try {
            paymentService.deleteCoupon(couponId);
            return ResponseEntity.ok("Coupon deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting coupon");
        }
    }


    //결제 데이터 세션 저장 로직 -------------------------------------------------------------------------------
//    @PostMapping("/request")
//    public ResponseEntity<String> requestPayment(@RequestBody PaymentRequest paymentRequest, HttpSession session) {
//        log.info("paymentRequest 정보 확인 : " + paymentRequest);
//        session.setAttribute("orderId", paymentRequest.getOrderId());
//        session.setAttribute("amount", paymentRequest.getAmount());
//        session.setAttribute("userEmail", paymentRequest.getUserEmail());
//        session.setAttribute("lecturePackageId", paymentRequest.getLecturePackageId());
//        session.setAttribute("paymentMethod", paymentRequest.getPaymentMethod());
//        session.setAttribute("couponId", paymentRequest.getCouponId());
//        session.setAttribute("priceKind", paymentRequest.getPriceKind());
//        log.info("세션 아이디 확인 : " + session.getId());
//        log.info("session 정보 확인 : " + session.getAttribute("orderId"));
//        log.info("session 정보 확인2 : " + session.getAttribute("lecturePackageId"));
//        return ResponseEntity.ok("Payment requested");
//    }
//
//    @GetMapping("/session-info")
//    public ResponseEntity<Map<String, Object>> getSessionInfo(HttpSession session) {
//        Map<String, Object> sessionInfo = new HashMap<>();
//        sessionInfo.put("orderId", session.getAttribute("orderId"));
//        sessionInfo.put("amount", session.getAttribute("amount"));
//        sessionInfo.put("userEmail", session.getAttribute("userEmail"));
//        sessionInfo.put("lecturePackageId", session.getAttribute("lecturePackageId"));
//        sessionInfo.put("paymentMethod", session.getAttribute("paymentMethod"));
//        sessionInfo.put("couponId", session.getAttribute("couponId"));
//        sessionInfo.put("priceKind", session.getAttribute("priceKind"));
//        log.info("세션 정보 가져오기 아이디 확인 : "+ session.getId());
//        log.info("세션 가져오기 정보 확인 : " + session.getAttribute("orderId"));
//        log.info("세션 가져오기 : " + sessionInfo);
//        return ResponseEntity.ok(sessionInfo);
//    }
//
//    @PostMapping("/clear-session")
//    public ResponseEntity<String> clearSession(HttpSession session) {
//        session.removeAttribute("orderId");
//        session.removeAttribute("amount");
//        session.removeAttribute("userEmail");
//        session.removeAttribute("lecturePackageId");
//        session.removeAttribute("paymentMethod");
//        session.removeAttribute("couponId");
//        session.removeAttribute("priceKind");
//
//        return ResponseEntity.ok("Session cleared");
//    }
}

