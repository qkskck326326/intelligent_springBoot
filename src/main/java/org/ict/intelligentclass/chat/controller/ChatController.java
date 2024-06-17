package org.ict.intelligentclass.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.ChatUserEntity;
import org.ict.intelligentclass.chat.jpa.entity.ChatroomEntity;
import org.ict.intelligentclass.chat.model.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/chat")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/countunreadall")
    public ResponseEntity<Long> countUnreadAll (@RequestParam String userId) {
        log.info("countUnreadAll start");
        Long countTotalUnRead = chatService.selectRoomIds(userId);

        log.info(countTotalUnRead.toString());

        return ResponseEntity.ok(countTotalUnRead);

    }


}
