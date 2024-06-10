package org.ict.intelligentclass.announcement.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.announcement.jpa.entity.AnnouncementEntity;
import org.ict.intelligentclass.announcement.jpa.repository.AnnouncementRepository;
import org.ict.intelligentclass.announcement.model.dto.AnnouncementDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public ArrayList<AnnouncementDto> selectAnnouncements(int page) {

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AnnouncementEntity> announcements = announcementRepository.findAll(pageable);
        ArrayList<AnnouncementDto> announcementDtos = new ArrayList<>();

        for (AnnouncementEntity announcement : announcements) {
            AnnouncementDto announcementDto = announcement.toDto();
            announcementDtos.add(announcementDto);
        }
        return announcementDtos;

    }

    public List<AnnouncementEntity> selectCategorizedAnnouncements(int page, Long category) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AnnouncementEntity> announcements = announcementRepository.findByCategory(category, pageable);
        return announcements.getContent();
    }

    public List<AnnouncementEntity> selectAnnouncementsByKeyword(int page, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AnnouncementEntity> announcements = announcementRepository.searchByKeyword(keyword, pageable);
        return announcements.getContent();
    }

    public AnnouncementEntity insertAnnouncement(AnnouncementEntity announcementEntity) {

        return announcementRepository.save(announcementEntity);
    }
}
