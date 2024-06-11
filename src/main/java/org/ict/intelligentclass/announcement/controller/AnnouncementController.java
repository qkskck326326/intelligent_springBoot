package org.ict.intelligentclass.announcement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.announcement.jpa.entity.AnnouncementEntity;
import org.ict.intelligentclass.announcement.model.dto.AnnouncementDto;
import org.ict.intelligentclass.announcement.model.service.AnnouncementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/announcement")
@RequiredArgsConstructor
@CrossOrigin
public class AnnouncementController {

    private final AnnouncementService announcementService;

    @GetMapping("/id")
    public ResponseEntity<AnnouncementEntity> getOneAnnouncement(@RequestParam int announcementId) {
        log.info(String.valueOf(announcementId));
        AnnouncementEntity entity = announcementService.selectOneAnnouncement(announcementId);
        return ResponseEntity.ok(entity);
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementDto>> getAnnouncements(@RequestParam int page){
        log.info("this happened");
        ArrayList<AnnouncementDto> list = announcementService.selectAnnouncements(page);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/categorized")
    public ResponseEntity<List<AnnouncementEntity>> getCategorizedAnnouncements(@RequestParam int page, @RequestParam long category){
        log.info("page: " + page + "category: " + category);
        System.out.println("page: " + page + "category: " + category);
        List<AnnouncementEntity> list = announcementService.selectCategorizedAnnouncements(page, category);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AnnouncementEntity>> getAnnouncementsByKeyword(@RequestParam int page, @RequestParam String keyword){

        List<AnnouncementEntity> list = announcementService.selectAnnouncementsByKeyword(page, keyword);
        return ResponseEntity.ok(list);
    }
    @PostMapping
    public ResponseEntity<AnnouncementEntity> insertAnnouncement(@RequestBody AnnouncementEntity announcementEntity){
        log.info(announcementEntity.toString());
        AnnouncementEntity entity = announcementService.insertAnnouncement(announcementEntity);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);

    }

    @PutMapping
    public ResponseEntity<AnnouncementEntity> updateAnnouncement(@RequestBody AnnouncementEntity announcementEntity){
        log.info(announcementEntity.toString());
        AnnouncementEntity entity = announcementService.updateAnnouncement(announcementEntity);
        return new ResponseEntity<>(entity, HttpStatus.ACCEPTED);

    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAnnouncement(@RequestBody AnnouncementEntity announcementEntity){
        log.info(announcementEntity.getAnnouncementId().toString());
        announcementService.deleteAnnouncement(announcementEntity.getAnnouncementId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
