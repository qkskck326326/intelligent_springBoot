package org.ict.intelligentclass.itnewssite.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ict.intelligentclass.itnewsboard.model.service.ItNewsBoardService;
import org.ict.intelligentclass.itnewssite.model.dto.ItNewsSiteDto;
import org.ict.intelligentclass.itnewssite.model.service.ItNewsSiteService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/itNewsBoard")
@RequiredArgsConstructor
@CrossOrigin
public class ItNewsSiteController {

    private final ItNewsSiteService itNewsSiteService;

    @GetMapping
    public ResponseEntity<List<ItNewsSiteDto>> getItNewsBoardList(Pageable pageable) {
        log.info("Fetching IT news board list with pageable: {}", pageable);
        List<ItNewsSiteDto> list = itNewsSiteService.getItNewsSiteList(pageable);
        return ResponseEntity.ok(list);
    }//

}//
