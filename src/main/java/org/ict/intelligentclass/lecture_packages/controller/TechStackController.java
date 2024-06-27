package org.ict.intelligentclass.lecture_packages.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.entity.TechStackEntity;
import org.ict.intelligentclass.lecture_packages.model.service.TechStackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/techstacks")
public class TechStackController {

    private final TechStackService techStackService;

    @GetMapping
    public ResponseEntity<List<TechStackEntity>> getTechStackAll() {
        List<TechStackEntity> techStack = techStackService.getTechStackAll();
        return ResponseEntity.ok(techStack);
    }

    @PostMapping
    public ResponseEntity<TechStackEntity> addTechStack(@RequestBody TechStackEntity techStackRequest) {
        TechStackEntity techStack = techStackService.addTechStack(techStackRequest);
        return ResponseEntity.ok(techStack);
    }




    @DeleteMapping
    public ResponseEntity<Void> deleteTechStack(@RequestParam Long techStackId) {
        log.info("Deleting techstack with id {}", techStackId);
        techStackService.deleteTechStack(techStackId);
        log.info("techStackId :", techStackId);
        return ResponseEntity.noContent().build();
    }

}
