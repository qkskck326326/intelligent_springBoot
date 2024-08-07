package org.ict.intelligentclass.certificate.model.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.ict.intelligentclass.certificate.jpa.entity.MyCertificateEntity;
import org.ict.intelligentclass.certificate.jpa.repository.MyCertificateRepository;
import org.ict.intelligentclass.certificate.model.dto.MyCertificateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.json.XMLTokener.entity;


@RequiredArgsConstructor
@Service
public class MyCertificateService {


    private static final Logger log = LoggerFactory.getLogger(MyCertificateService.class);
    private final MyCertificateRepository myCertificateRepository;


    public List<MyCertificateDto> getCertificatesByNickname(String nickname) {
        log.info("nickname : ", nickname);

        List<MyCertificateEntity> myCertificateEntities = myCertificateRepository.findByNickname(nickname);
        ArrayList<MyCertificateDto> list = new ArrayList<>();
        for(MyCertificateEntity entity : myCertificateEntities){
            MyCertificateDto myCertificateDto = entity.toDto();
            list.add(myCertificateDto);
        }
        log.info("list : ", list);
        return list;
    }

    public MyCertificateDto addCertificate(MyCertificateDto certificateDTO) {
        MyCertificateEntity entity = certificateDTO.toEntity();
        MyCertificateEntity savedEntity = myCertificateRepository.save(entity);
        return MyCertificateDto.fromEntity(savedEntity);
    }


    public MyCertificateDto updateCertificate(MyCertificateDto certificateDTO) {
        Optional<MyCertificateEntity> existingEntityOpt = myCertificateRepository.findById(certificateDTO.getCertificateNumber());

        if (existingEntityOpt.isPresent()) {
            MyCertificateEntity existingEntity = existingEntityOpt.get();
            existingEntity.setPdfFile(certificateDTO.getPdfFile());
            existingEntity.setKind(certificateDTO.getKind());
            existingEntity.setPassDate(certificateDTO.getPassDate());
            existingEntity.setIssuePlace(certificateDTO.getIssuePlace());

            MyCertificateEntity updatedEntity = myCertificateRepository.save(existingEntity);
            return MyCertificateDto.fromEntity(updatedEntity);
        } else {

            throw new RuntimeException("Certificate not found");
        }
    }




    public void deleteCertificate(String certificateNumber) {
        myCertificateRepository.deleteById(certificateNumber);

    }
}