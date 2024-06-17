package org.ict.intelligentclass.lecture_packages.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.entity.TechStackEntity;
import org.ict.intelligentclass.lecture_packages.model.service.TechStackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
