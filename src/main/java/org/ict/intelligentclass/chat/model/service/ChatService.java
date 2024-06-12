package org.ict.intelligentclass.chat.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ict.intelligentclass.chat.jpa.entity.ChatUserEntity;
import org.ict.intelligentclass.chat.jpa.entity.ChatroomEntity;
import org.ict.intelligentclass.chat.jpa.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatroomRepository chatroomRepository;
    private final ChatUserRepository chatUserRepository;
    private final MessageFileRepository messageFileRepository;
    private final MessageReadRepository messageReadRepository;

    public Long selectRoomIds(String userId) {

        log.info("selectRoomIds userId = " + userId);
        //챗유저 닉네임으로 모든 채팅방 아이디 가져오기
        List<Long> chatroomIds = chatUserRepository.findRoomIdsByUserId(userId);

        log.info("선택된 챗방 아이디들: {}", chatroomIds);

        //만약 아무것도 없다면 리턴
        if(chatroomIds == null || chatroomIds.isEmpty()) {
            return 0L;

        } else {
            //있을 경우 하나씩 꺼내서 채팅방 확인
            long sum = 0;
            for (Long chatroomId : chatroomIds) {

                //전체 메시지 갯수
                long eachTotalMessageCount = chatMessageRepository.countByRoomId(chatroomId);

                //읽음 메시지 확인
                long eachTotalReadMessageCount = messageReadRepository.countByRoomId(chatroomId);

                long UnreadMessageCount = eachTotalMessageCount - eachTotalReadMessageCount;
                sum += UnreadMessageCount;
            }
            return sum;
        }
    }


}
