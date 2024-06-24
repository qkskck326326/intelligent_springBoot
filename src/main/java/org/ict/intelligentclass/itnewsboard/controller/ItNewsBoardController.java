package org.ict.intelligentclass.itnewsboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.itnewsboard.jpa.entity.ItNewsBoardEntity;
import org.ict.intelligentclass.itnewsboard.model.dto.ItNewsBoardDto;

import org.ict.intelligentclass.itnewsboard.model.service.ItNewsBoardService;
import org.ict.intelligentclass.itnewssite.jpa.entity.ItNewsSiteEntity;
import org.ict.intelligentclass.itnewssite.model.dto.ItNewsSiteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/itNewsBoard")
@RequiredArgsConstructor
@CrossOrigin
public class ItNewsBoardController {

    private final ItNewsBoardService itNewsBoardService;

    @GetMapping
    public ResponseEntity<Page<ItNewsBoardEntity>> getItNewsBoardList(Pageable pageable) {
        log.info("Fetching IT news board list with pageable: {}", pageable);
        Page<ItNewsBoardEntity> list = itNewsBoardService.getItNewsBoardList(pageable);
        return ResponseEntity.ok(list);
    }//

    @GetMapping("/search/{title}")
    public ResponseEntity<Page<ItNewsBoardEntity>> searchItNewsBoard(@PathVariable String title,Pageable pageable) {
        log.info("Fetching IT news site list with pageable: {}", pageable);
        Page<ItNewsBoardEntity> list  = itNewsBoardService.findByTitleContaining(title, pageable);
        return ResponseEntity.ok(list);
    }


    @GetMapping("/{no}")
    public ResponseEntity<ItNewsBoardDto> getItNewsBoard(@PathVariable("no") Long no) {
        log.info("Fetching IT news board with no: {}", no);
        return ResponseEntity.ok(itNewsBoardService.getItNewsBoard(no));
    }

    @PostMapping
    public ResponseEntity<ItNewsBoardDto> saveBoard(@RequestBody ItNewsBoardDto itNewsBoardDto) {
        ItNewsBoardDto savedBoard = itNewsBoardService.saveBoard(itNewsBoardDto);
        return new ResponseEntity<>(savedBoard, HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBoard(@RequestBody ItNewsBoardDto itNewsBoardDto) {
        log.info("Deleting IT news site: {}", itNewsBoardDto);
        itNewsBoardService.deleteBoard(itNewsBoardDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //

}//
