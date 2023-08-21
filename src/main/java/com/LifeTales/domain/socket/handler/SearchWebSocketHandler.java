package com.LifeTales.domain.socket.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class SearchWebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session , message);
        System.out.println("WebSocket connection established: " + session.getId());
        // 클라이언트로부터 메시지 수신 및 처리
        String payload = message.getPayload();

        // 받은 메시지를 백엔드 서버 로그로 출력
        System.out.println("Received message: " + payload);

        // 클라이언트로 응답 메시지 전송
        session.sendMessage(new TextMessage("Server received your message: " + payload));

    }
}
