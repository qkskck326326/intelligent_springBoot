package org.ict.intelligentclass.chat.controller;

import org.ict.intelligentclass.chat.model.dto.*;
import org.ict.intelligentclass.chat.model.service.WebSocketService;
import org.ict.intelligentclass.user.jpa.entity.UserEntity;
import org.ict.intelligentclass.user.model.service.UserService;
import org.springframework.http.HttpHeaders;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.*;
import org.ict.intelligentclass.chat.model.service.ChatService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/chat")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final Path fileStorageLocation = Paths.get("src/main/resources/static/uploads").toAbsolutePath().normalize();
    private final WebSocketService webSocketService;
    private final UserService userService;

    @GetMapping("/countunreadall")
    public ResponseEntity<Long> countUnreadAll(@RequestParam String userId) {

        log.info("countUnreadAll start");
        Long countTotalUnRead = chatService.countTotalUnread(userId);
        log.info("countUnreadAll end" + countTotalUnRead);
        return ResponseEntity.ok(countTotalUnRead);

    }

    @PostMapping("/makechat/{roomType}")
    public ResponseEntity<ChatroomEntity> makechat(@PathVariable String roomType, @RequestBody MakeChatDto request) {
        List<String> names = request.getNames();
        log.info("makechat start " + names + " " + roomType);
        ChatroomEntity entity = chatService.insertRoom(names, roomType);
        webSocketService.broadcastUpdate();
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @GetMapping("/chatlist")
    public ResponseEntity<List<ChatroomDetailsDto>> listChatroom(@RequestParam String userId, @RequestParam boolean isChats) {
        log.info("listChatroom start");
        List<ChatroomDetailsDto> chatrooms = chatService.getChatrooms(userId, isChats);
        return ResponseEntity.ok(chatrooms);
    }

    @GetMapping("/chatdata")
    public ResponseEntity<ChatMessagesResponse> getMessages(@RequestParam String userId, @RequestParam Long roomId, @RequestParam int page) {
        ChatMessagesResponse res = chatService.getMessages(userId, roomId, page);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<String>> getAdmins() {
        return ResponseEntity.ok(userService.getAdmins(2));
    }

    @GetMapping("/people")
    public ResponseEntity<List<UserEntity>> getPeople(@RequestParam Long roomId) {
        List<UserEntity> users = chatService.getPeople(roomId);
        return ResponseEntity.ok(users);
    }

    @PostMapping(value = "/sendmessage")
    public ResponseEntity<ChatMessageDto> sendMessage(
            @RequestBody ChatMessageEntity chatMessageEntity) {
        log.info("Received message to send: {}", chatMessageEntity);

        ChatMessageEntity savedMessage = chatService.saveMessage(chatMessageEntity);

        log.info("Saved message: {}", savedMessage);

        ChatMessageDto messageDto = chatService.convertToDto(savedMessage);

        log.info("converted messageDto: {}", messageDto);

        webSocketService.sendToSpecificRoom(savedMessage.getRoomId(), messageDto);
        //이걸로 업데이트가 일어남을 알림
        webSocketService.broadcastUpdate();

        return ResponseEntity.ok(messageDto);
    }

    @PostMapping("markread")
    public ResponseEntity<MessageReadEntity> markRead(@RequestBody ChatMessageEntity chatMessageEntity) {
        log.info("markRead start");
        log.info("markRead {}", chatMessageEntity);

        MessageReadCompositeKey compositeKey = new MessageReadCompositeKey(chatMessageEntity.getMessageId(), chatMessageEntity.getSenderId());
        Long roomId = chatMessageEntity.getRoomId();
        MessageReadEntity messageRead = chatService.markRead(compositeKey, roomId);
        return ResponseEntity.ok(messageRead);
    }


    @PostMapping(value = "/uploadfiles/{roomId}/{senderId}/{messageType}/{dateSent}/{isAnnouncement}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadFiles(
            @PathVariable Long roomId,
            @PathVariable String senderId,
            @PathVariable Long messageType,
            @PathVariable String dateSent,
            @PathVariable Long isAnnouncement,
            @RequestParam(value = "files") List<MultipartFile> files) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        Date parsedDateSent = sdf.parse(dateSent);

        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.setRoomId(roomId);
        chatMessageEntity.setSenderId(senderId);
        chatMessageEntity.setMessageType(messageType);
        chatMessageEntity.setDateSent(parsedDateSent);
        chatMessageEntity.setIsAnnouncement(isAnnouncement);

        // Save the message first to generate the message ID
        ChatMessageEntity savedMessage = chatService.saveMessage(chatMessageEntity);

        List<MessageFileEntity> fileEntities = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            log.info("파일 프로세싱");
            fileEntities = chatService.saveFiles(savedMessage, files);
        }

        ChatResponse response = new ChatResponse(savedMessage, fileEntities);

        ChatMessageDto messageDto = chatService.convertToDto(savedMessage);

        webSocketService.sendToSpecificRoom(roomId, messageDto);

        return ResponseEntity.ok(response);
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

            RoomNameChangeDto roomNameChangeDto = new RoomNameChangeDto(roomId, roomName);
            webSocketService.sendRoomNameChange(roomId, roomNameChangeDto);
            webSocketService.broadcastUpdate();
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
        ChatMessageDto messageDto = chatService.convertToDto(updatedAnnouncement);
        webSocketService.sendToSpecificRoom(roomId, messageDto);
        return ResponseEntity.ok(updatedAnnouncement);
    }

    @PutMapping("/delete/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId) {
        ChatMessageEntity deletedMessage = chatService.deleteMessage(messageId);
        ChatMessageDto messageDto = chatService.convertToDto(deletedMessage);
        webSocketService.sendToSpecificRoom(deletedMessage.getRoomId(), messageDto);
        webSocketService.broadcastUpdate();
        return ResponseEntity.ok(deletedMessage);
    }

    @DeleteMapping("/leaveroom")
    public ResponseEntity<?> leaveRoom(@RequestBody Map<String, Object> request) {

        String userId = (String) request.get("userId");
        Long roomId = ((Number) request.get("roomId")).longValue();

        try {
            chatService.leaveRoom(userId, roomId);
            webSocketService.broadcastUpdate();
            return ResponseEntity.ok("User has left the room");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Path filePath = fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();

        }
    }
}