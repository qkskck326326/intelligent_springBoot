package org.ict.intelligentclass.payment.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.output.LecturePackageDetail;
import org.ict.intelligentclass.lecture_packages.model.service.LecturePackageService;
import org.ict.intelligentclass.payment.jpa.entity.CouponEntity;
import org.ict.intelligentclass.payment.jpa.entity.PaymentEntity;
import org.ict.intelligentclass.payment.jpa.repository.PaymentRepository;
import org.ict.intelligentclass.payment.model.dto.ConfirmDto;
import org.ict.intelligentclass.payment.model.dto.PaymentHistoryDto;
import org.ict.intelligentclass.payment.model.dto.PaymentRequest;
import org.ict.intelligentclass.payment.model.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/transaction/{userEmail}/{provider}/{lecturePackageId}")
    public ResponseEntity<?> getTransaction(@PathVariable String userEmail, @PathVariable String provider, @PathVariable Long lecturePackageId) {
        Optional<PaymentEntity> transaction = paymentService.getTransaction(userEmail, provider, lecturePackageId);
        if (transaction.isPresent()) {
            return ResponseEntity.ok(transaction.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/transaction/{userEmail}/{provider}/{lecturePackageId}")
    public ResponseEntity<?> updateTransaction(@PathVariable String userEmail, @PathVariable String provider, @PathVariable Long lecturePackageId, @RequestBody PaymentEntity paymentInfo) {
        boolean updated = paymentService.updateTransaction(userEmail, provider, lecturePackageId, paymentInfo);
        if (updated) {
            return ResponseEntity.ok("Transaction updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/updateConfirmation/{transactionId}")
    public ResponseEntity<String> updatePaymentConfirmation(@PathVariable Long transactionId, @RequestBody Map<String, String> updateFields) {
        Optional<PaymentEntity> paymentEntityOptional = paymentRepository.findById(transactionId);
        if (paymentEntityOptional.isPresent()) {
            PaymentEntity paymentEntity = paymentEntityOptional.get();
            if (updateFields.containsKey("paymentConfirmation")) {
                paymentEntity.setPaymentConfirmation(updateFields.get("paymentConfirmation"));
            }
            paymentRepository.save(paymentEntity);
            return ResponseEntity.ok("Payment confirmation updated successfully");
        } else {
            return ResponseEntity.status(404).body("Payment not found for this id :: " + transactionId);
        }
    }


    @GetMapping("paymentHistory/{userEmail}")
    public ResponseEntity<List<PaymentHistoryDto>> getTransactionHistory(@PathVariable String userEmail) {
        log.info("userEmail {}: " + userEmail); // 요청된 userEmail 로그 출력
        List<PaymentHistoryDto> history = paymentService.getTransactionHistoryByUserEmail(userEmail);
        return ResponseEntity.ok(history);
    }

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

    @PostMapping("/confirmation")
    public ResponseEntity<ConfirmDto> confirmation(@RequestBody ConfirmDto confirmDto) {

        ConfirmDto confirm = paymentService.getConfirmation(confirmDto);

        return ResponseEntity.ok(confirm);
    }



}

