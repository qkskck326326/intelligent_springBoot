package org.ict.intelligentclass.itnewssite.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ict.intelligentclass.itnewssite.jpa.entity.ItNewsSiteEntity;
import org.ict.intelligentclass.itnewssite.jpa.repository.ItNewsSiteRepository;
import org.ict.intelligentclass.itnewssite.model.dto.ItNewsSiteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItNewsSiteService {
    private final ItNewsSiteRepository itNewsSiteRepository;

    public List<ItNewsSiteDto> getItNewsSiteList(Pageable pageable) {
        Page<ItNewsSiteEntity> pageResult = itNewsSiteRepository.findAll(pageable);
        return pageResult.stream()
                .map(ItNewsSiteEntity::toDto)
                .collect(Collectors.toList());
    }//

    public ItNewsSiteDto saveSite(ItNewsSiteDto itNewsSiteDto) {
        return itNewsSiteRepository.save(itNewsSiteDto.toEntity()).toDto();
    }

    public void deleteSite(ItNewsSiteDto itNewsSiteDto) {
        itNewsSiteRepository.delete(itNewsSiteDto.toEntity());
    }

    public List<ItNewsSiteEntity> findBySiteNameContaining(String siteName, Pageable pageable) {
        return  itNewsSiteRepository.findBySiteNameContaining(siteName, pageable);
    }
}//
