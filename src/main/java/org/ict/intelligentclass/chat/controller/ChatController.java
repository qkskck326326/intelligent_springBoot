package org.ict.intelligentclass.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.ChatMessageEntity;
import org.ict.intelligentclass.chat.model.dto.ChatMessagesResponse;
import org.ict.intelligentclass.chat.model.dto.ChatroomDetailsDto;
import org.ict.intelligentclass.chat.jpa.entity.ChatroomEntity;
import org.ict.intelligentclass.chat.model.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<ChatroomDetailsDto>> listChatroom (@RequestParam String userId) {
        log.info("listChatroom start");
        List<ChatroomDetailsDto> chatrooms = chatService.getChatrooms(userId);
        return ResponseEntity.ok(chatrooms);
    }

    @GetMapping("/chatdata")
    public ResponseEntity<ChatMessagesResponse> getMessages(@RequestParam String userId, @RequestParam Long roomId, @RequestParam int page) {
        ChatMessagesResponse response = chatService.getMessages(userId, roomId, page);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/sendmessage")
    public ResponseEntity<ChatMessageEntity> sendMessage(@RequestBody ChatMessageEntity chatMessageEntity) {
        ChatMessageEntity savedMessage = chatService.saveMessage(chatMessageEntity);
        return ResponseEntity.ok(savedMessage);
    }


}
