package org.ict.intelligentclass.itnewsboard.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ict.intelligentclass.itnewsboard.jpa.entity.ItNewsBoardEntity;
import org.ict.intelligentclass.itnewsboard.jpa.repository.ItNewsBoardRepository;
import org.ict.intelligentclass.itnewsboard.model.dto.ItNewsBoardDto;

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
public class ItNewsBoardService {
    private final ItNewsBoardRepository itNewsBoardRepository;

    public Page<ItNewsBoardEntity> getItNewsBoardList(Pageable pageable) {
        Pageable sortedByDateDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("registDate").descending()
        );
        return itNewsBoardRepository.findAll(sortedByDateDesc);
    }//

    public Page<ItNewsBoardEntity> findByTitleContaining(String title, Pageable pageable) {
        return itNewsBoardRepository.findByTitleContaining(title, pageable);
    }

    public ItNewsBoardDto saveBoard(ItNewsBoardDto itNewsBoardDto) {
        return itNewsBoardRepository.save(itNewsBoardDto.toEntity()).toDto();
    }

    public void deleteBoard(ItNewsBoardDto itNewsBoardDto) {
        itNewsBoardRepository.delete(itNewsBoardDto.toEntity());
    }

    public ItNewsBoardDto getItNewsBoard(Long no) {
        return itNewsBoardRepository.findById(no).get().toDto();
    }


}//
