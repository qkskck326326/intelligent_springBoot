package org.ict.intelligentclass.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.ChatUserEntity;
import org.ict.intelligentclass.chat.jpa.entity.ChatroomEntity;
import org.ict.intelligentclass.chat.model.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @PostMapping("makechat/{roomType}")
    public ResponseEntity<ChatroomEntity> makechat (@PathVariable String roomType, @RequestBody Map<Integer, String> people) {
        log.info("makechat start" + people + roomType);
        ChatroomEntity entity = chatService.insertRoom(people, roomType);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @GetMapping("/chatlist")
    public ResponseEntity<List<ChatroomEntity>> listChatroom (@RequestParam String userId) {
        log.info("listChatroom start");
        List<ChatroomEntity> entities = chatService.getChatrooms(userId);
        return ResponseEntity.ok(entities);
    }


}
