package org.ict.intelligentclass.lecture_packages.model.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.lecture_packages.jpa.entity.TechStackEntity;
import org.ict.intelligentclass.lecture_packages.jpa.repository.TechStackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class TechStackService {

    private final TechStackRepository techStackRepository;


    public List<TechStackEntity> getTechStackAll(){
        return techStackRepository.findAll();


    }

    public TechStackEntity addTechStack(TechStackEntity techStackEntity) {
//        TechStackEntity techStack = TechStackEntity.builder()
//                .techStackName(techStackEntity.getTechStackName())
//                .techStackPath(techStackEntity.getTechStackPath())
//                .build();
        return techStackRepository.save(techStackEntity);
    }

    public void deleteTechStack(Long techStackId) {
        techStackRepository.deleteById(techStackId);
    }
}
