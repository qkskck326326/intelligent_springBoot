package org.ict.intelligentclass.career.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.career.jpa.entity.CareerEntity;
import org.ict.intelligentclass.career.model.service.CareerService;
import org.ict.intelligentclass.education.jpa.entity.EducationEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/careers")
public class careerController {

    private final CareerService careerService;

    @GetMapping
    public ResponseEntity<List<CareerEntity>> getAllEducations(@RequestParam String nickname) {
        List<CareerEntity> careerEntitys = careerService.getAllCareers(nickname);
        return ResponseEntity.ok(careerEntitys);
    }

    @PostMapping
    public ResponseEntity<CareerEntity> createEducation(@RequestBody CareerEntity career) {
        CareerEntity careerEntity = careerService.createCareer(career);
        return ResponseEntity.ok(careerEntity);
    }

    @PutMapping
    public ResponseEntity<CareerEntity> updateEducation(@RequestBody CareerEntity career) {
        CareerEntity updatedCareer = careerService.updateCareerById(career);
        if (updatedCareer != null) {
            return ResponseEntity.ok(updatedCareer);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{careerId}")
    public ResponseEntity<Void> deleteCareerId(@PathVariable Long careerId) {
        careerService.deleteCareerById(careerId);
        return ResponseEntity.noContent().build();
    }
}
