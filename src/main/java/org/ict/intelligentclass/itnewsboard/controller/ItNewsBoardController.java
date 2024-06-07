package org.ict.intelligentclass.itnewsboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.itnewsboard.model.dto.ItNewsBoardDto;
import org.ict.intelligentclass.itnewsboard.model.service.ItNewsBoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/itNewsBoard")
@RequiredArgsConstructor
@CrossOrigin
public class ItNewsBoardController {

    private final ItNewsBoardService itNewsBoardService;

    @GetMapping
    public ResponseEntity<List<ItNewsBoardDto>> getItNewsBoardList(Pageable pageable) {
        log.info("Fetching IT news board list with pageable: {}", pageable);
        List<ItNewsBoardDto> list = itNewsBoardService.getItNewsBoardList(pageable);
        return ResponseEntity.ok(list);
    }//

}//
