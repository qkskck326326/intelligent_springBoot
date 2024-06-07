package org.ict.intelligentclass.certificate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
import org.ict.intelligentclass.certificate.model.dto.MyCertificateDto;
import org.ict.intelligentclass.certificate.model.service.MyCertificateService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
@CrossOrigin
public class MyCertificateController {

    private final MyCertificateService myCertificateService;

    @GetMapping
    public ResponseEntity<Page<MyCertificateEntity>> getCertificatesByNickname(
            @RequestParam String nickname,
            @RequestParam(name="page") int page,
            @RequestParam(name="size") int size) {
        log.info("nickname : "+ nickname);
        Page<MyCertificateEntity> certificates = myCertificateService.getCertificatesByNickname(nickname, page, size);
        return ResponseEntity.ok(certificates);
    }

    @PostMapping
    public ResponseEntity<MyCertificateDto> addCertificate(@RequestBody MyCertificateDto certificateDTO) {
        MyCertificateDto newCertificate = myCertificateService.addCertificate(certificateDTO);
        return new ResponseEntity<>(newCertificate, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCertificate(@PathVariable String id) {
        myCertificateService.deleteCertificate(id);
        return ResponseEntity.ok().build();
    }
}