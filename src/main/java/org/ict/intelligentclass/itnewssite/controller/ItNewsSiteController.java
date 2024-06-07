package org.ict.intelligentclass.itnewssite.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.ict.intelligentclass.itnewssite.model.dto.ItNewsSiteDto;
import org.ict.intelligentclass.itnewssite.model.service.ItNewsSiteService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/itNewsSite")
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

    @PostMapping
    public ResponseEntity<ItNewsSiteDto> saveSite(@RequestBody ItNewsSiteDto itNewsSiteDto) {
        ItNewsSiteDto savedSite = itNewsSiteService.saveSite(itNewsSiteDto);
        return new ResponseEntity<>(savedSite, HttpStatus.CREATED);
    }




}//
