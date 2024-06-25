package org.ict.intelligentclass.education.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.education.jpa.entity.EducationEntity;

import org.ict.intelligentclass.education.model.service.EducationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/educations")
public class EducationController {

    private final EducationService educationService;

    @GetMapping
    public ResponseEntity<List<EducationEntity>> getAllEducations(@RequestParam String nickname) {
        List<EducationEntity> educations = educationService.getAllEducations(nickname);
        log.info("getAllEducations: " + educations);
        return ResponseEntity.ok(educations);
    }

    @GetMapping("/detail")
    public ResponseEntity<EducationEntity> getDetailducationId(@RequestParam Long educationId) {
        EducationEntity education = educationService.getDetaill(educationId);
        return ResponseEntity.ok(education);
    }

    @PostMapping
    public ResponseEntity<EducationEntity> createEducation(@RequestBody EducationEntity education) {
        EducationEntity educationEntity = educationService.createEducation(education);
        log.info("createEducation: " + educationEntity);
        return ResponseEntity.ok(educationEntity);
    }

    @PutMapping
    public ResponseEntity<EducationEntity> updateEducation(@RequestBody EducationEntity education) {
        EducationEntity updatedEducation = educationService.updateEducationByNickname(education);
        if (updatedEducation != null) {
            return ResponseEntity.ok(updatedEducation);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{educationId}")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long educationId) {
        educationService.deleteEducationById(educationId);
        return ResponseEntity.noContent().build();
    }
}
