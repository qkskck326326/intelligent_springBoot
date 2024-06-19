package org.ict.intelligentclass.chat.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.ChatMessageEntity;
import org.ict.intelligentclass.chat.jpa.entity.ChatUserCompositeKey;
import org.ict.intelligentclass.chat.jpa.entity.ChatUserEntity;
import org.ict.intelligentclass.chat.model.dto.ChatMessagesResponse;
import org.ict.intelligentclass.chat.model.dto.ChatroomDetailsDto;
import org.ict.intelligentclass.chat.jpa.entity.ChatroomEntity;
import org.ict.intelligentclass.chat.model.dto.MakeChatDto;
import org.ict.intelligentclass.chat.model.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @PostMapping("/makechat/{roomType}")
    public ResponseEntity<ChatroomEntity> makechat(@PathVariable String roomType, @RequestBody MakeChatDto request) {
        List<String> names = request.getNames();
        log.info("makechat start " + names + " " + roomType);
        ChatroomEntity entity = chatService.insertRoom(names, roomType);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

//    @PostMapping("makechat/{roomType}")
//    public ResponseEntity<ChatroomEntity> makechat (@PathVariable String roomType, @RequestBody Map<Integer, String> people) {
//        log.info("makechat start" + people + roomType);
//        ChatroomEntity entity = chatService.insertRoom(people, roomType);
//        return new ResponseEntity<>(entity, HttpStatus.CREATED);
//    }

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

    @GetMapping("/chatuserdetail")
    public ResponseEntity<ChatUserEntity> getChatUserDetail(@RequestParam String userId, @RequestParam Long roomId) {
        log.info("getChatUserDetail start");
        ChatUserEntity entity = chatService.getChatUserDetail(userId, roomId);
        return ResponseEntity.ok(entity);
    }

    @PutMapping("/changepin")
    public ResponseEntity<?> changePinStatus(@RequestBody Map<String, Object> request) {

        String userId = (String) request.get("userId");
        Long roomId = ((Number) request.get("roomId")).longValue();
        Long isPinned = ((Number) request.get("isPinned")).longValue();

        try {
            ChatUserEntity updatedChatUser = chatService.changePinStatus(userId, roomId, isPinned);
            return ResponseEntity.ok(updatedChatUser);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/changeroomname")
    public ResponseEntity<?> changeRoomName(@RequestBody Map<String, Object> request) {

        Long roomId = ((Number) request.get("roomId")).longValue();
        String roomName = (String) request.get("roomName");


        try {
            ChatroomEntity updatedChatRoom = chatService.changeRoomName(roomId, roomName);
            return ResponseEntity.ok(updatedChatRoom);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PutMapping("/announce")
    public ResponseEntity<ChatMessageEntity> updateAnnouncement(@RequestBody Map<String, Object> request) {

        Long messageId = ((Number) request.get("messageId")).longValue();
        Long roomId = ((Number) request.get("roomId")).longValue();

        ChatMessageEntity updatedAnnouncement = chatService.updateAnnouncement(roomId, messageId);
        return ResponseEntity.ok(updatedAnnouncement);

    }

    @DeleteMapping("/leaveroom")
    public ResponseEntity<?> leaveRoom(@RequestBody Map<String, Object> request) {

        String userId = (String) request.get("userId");
        Long roomId = ((Number) request.get("roomId")).longValue();

        try {
            chatService.leaveRoom(userId, roomId);
            return ResponseEntity.ok("User has left the room");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
