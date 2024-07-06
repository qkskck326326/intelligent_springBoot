package org.ict.intelligentclass.complete.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.complete.jpa.entity.CompleteEntity;
import org.ict.intelligentclass.complete.model.service.CompleteService;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/completes")
@RequiredArgsConstructor
@CrossOrigin
public class CompleteController {

    private final CompleteService completeService;

    @GetMapping("/{nickname}")
    public ResponseEntity<List<CompleteEntity>> getCompletesByNickname(@PathVariable String nickname) {
        List<CompleteEntity> completes = completeService.getCompletesByNickname(nickname);
        return new ResponseEntity<>(completes, HttpStatus.OK);
    }

    @GetMapping("/user/{nickname}")
    public ResponseEntity<UserEntity> getUserByNickname(@PathVariable String nickname) {
        Optional<UserEntity> user = completeService.getUserByNickname(nickname);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
