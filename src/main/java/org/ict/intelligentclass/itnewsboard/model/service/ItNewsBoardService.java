package org.ict.intelligentclass.itnewsboard.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ict.intelligentclass.itnewsboard.jpa.entity.ItNewsBoardEntity;
import org.ict.intelligentclass.itnewsboard.jpa.repository.ItNewsBoardRepository;
import org.ict.intelligentclass.itnewsboard.model.dto.ItNewsBoardDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ItNewsBoardService {
    private final ItNewsBoardRepository itNewsBoardRepository;

    public List<ItNewsBoardDto> getItNewsBoardList(Pageable pageable) {
        Page<ItNewsBoardEntity> pageResult = itNewsBoardRepository.findAll(pageable);
        return pageResult.stream()
                .map(ItNewsBoardEntity::toDto)
                .collect(Collectors.toList());
    }//

}//
