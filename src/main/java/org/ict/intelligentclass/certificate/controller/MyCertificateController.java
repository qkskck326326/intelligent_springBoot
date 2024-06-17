package org.ict.intelligentclass.certificate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
import org.ict.intelligentclass.certificate.model.dto.MyCertificateDto;
import org.ict.intelligentclass.certificate.model.service.MyCertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
@CrossOrigin
public class MyCertificateController {

    private final MyCertificateService myCertificateService;

    @GetMapping
    public ResponseEntity<List<MyCertificateDto>> getCertificatesByNickname( @RequestParam String nickname) {
        log.info("nickname : ", nickname);

        List<MyCertificateDto> certificates = myCertificateService.getCertificatesByNickname(nickname);
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }



    @PostMapping
    public ResponseEntity<MyCertificateDto> addCertificate(@RequestBody MyCertificateDto certificateDTO) {
        MyCertificateDto newCertificate = myCertificateService.addCertificate(certificateDTO);
        return new ResponseEntity<>(newCertificate, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCertificate(@RequestParam String certificateNumber) {
        log.info("certificateNumber : ", certificateNumber);
        myCertificateService.deleteCertificate(certificateNumber);
        return ResponseEntity.ok().build();
    }
}