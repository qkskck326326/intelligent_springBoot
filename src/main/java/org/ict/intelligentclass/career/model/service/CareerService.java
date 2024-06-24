package org.ict.intelligentclass.career.model.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.career.jpa.entity.CareerEntity;
import org.ict.intelligentclass.career.jpa.repository.CareerRepository;
import org.ict.intelligentclass.education.jpa.entity.EducationEntity;
import org.ict.intelligentclass.education.jpa.repository.EducationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class CareerService {

    private final CareerRepository careerRepository;

    //닉네임으로 조회.
    public List<CareerEntity> getAllCareers(String nickname) {
        List<CareerEntity> careers = careerRepository.findByNickname(nickname);

        return careers;
    }


    public CareerEntity createCareer(CareerEntity career) {
        return careerRepository.save(career);
    }



    public CareerEntity updateCareerById(Long careerId, CareerEntity updatedCareer) {
        Optional<CareerEntity> careerEntityOptional = careerRepository.findById(careerId);

        if (careerEntityOptional.isPresent()) {
            CareerEntity careerEntity = careerEntityOptional.get();

            careerEntity.setInstitutionName(updatedCareer.getInstitutionName());
            careerEntity.setDepartment(updatedCareer.getDepartment());
            careerEntity.setPosition(updatedCareer.getPosition());
            careerEntity.setStartDate(updatedCareer.getStartDate());
            careerEntity.setEndDate(updatedCareer.getEndDate());
            careerEntity.setResponsibilities(updatedCareer.getResponsibilities());

            careerRepository.save(careerEntity);
            return careerEntity;
        } else {
            return null;
        }
    }

    public void deleteCareerById(Long careerId) {
        careerRepository.deleteById(careerId);
    }
}
