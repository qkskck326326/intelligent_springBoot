package org.ict.intelligentclass.certificate.model.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
import org.ict.intelligentclass.certificate.jpa.repository.MyCertificateRepository;
import org.ict.intelligentclass.certificate.model.dto.MyCertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class MyCertificateService {


    private MyCertificateRepository myCertificateRepository;


    public Page<MyCertificateEntity> getCertificatesByNickname(String nickname, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return myCertificateRepository.findByNickname(nickname, pageable);
    }

    public MyCertificateDto addCertificate(MyCertificateDto certificateDTO) {
        MyCertificateEntity entity = certificateDTO.toEntity();
        MyCertificateEntity savedEntity = myCertificateRepository.save(entity);
        return MyCertificateDto.fromEntity(savedEntity);
    }

    public void deleteCertificate(String id) {
        myCertificateRepository.deleteById(id);
    }
}