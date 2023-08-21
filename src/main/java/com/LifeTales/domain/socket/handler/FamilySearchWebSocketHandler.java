package com.LifeTales.domain.socket.handler;

import com.LifeTales.domain.family.repository.DTO.FamilySearchDTO;
import com.LifeTales.domain.socket.Service.SocketService.FindSocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FamilySearchWebSocketHandler extends TextWebSocketHandler {
    private final FindSocketService findSocketService;

    public FamilySearchWebSocketHandler(FindSocketService findSocketService) {
        this.findSocketService = findSocketService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session , message);
        log.info("FamilySearchWebSocketHandler - handleTextMessage Start");

        System.out.println("WebSocket connection established: " + session.getId());
        // 클라이언트로부터 메시지 수신 및 처리
        String payload = message.getPayload();

        // 받은 메시지를 백엔드 서버 로그로 출력
        System.out.println("Received message: " + payload);
        /**
         * Logic
         * 1. family List
         * 2. total result Score
         */

        // family List
        List<FamilySearchDTO> familyList = findSocketService.find_family_service(payload);
        // famiy total count
        int totalResultCount = familyList.size();

        for (FamilySearchDTO dto : familyList) {
            log.info("FamilySeq : {} NickName: {}", dto.getSeq(),dto.getNickName());
        }

        //

        // 클라이언트로 응답 메시지 전송
        String familyData = familyList.stream()
                .map(dto -> dto.getSeq() + ": " + dto.getNickName())
                .collect(Collectors.joining(", "));

        familyData = familyData +", "+"ResultCount: " + totalResultCount;

        session.sendMessage(new TextMessage(familyData));

    }
}
