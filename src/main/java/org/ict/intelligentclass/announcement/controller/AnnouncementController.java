package org.ict.intelligentclass.announcement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.announcement.model.service.AnnouncementService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/announcement")
@RequiredArgsConstructor
@CrossOrigin
public class AnnouncementController {

    private final AnnouncementService announcementService;


}
