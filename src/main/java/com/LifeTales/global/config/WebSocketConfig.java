package com.LifeTales.global.config;

import com.LifeTales.domain.socket.Service.SocketService.FindSocketService;
import com.LifeTales.domain.socket.handler.FamilySearchWebSocketHandler;
import com.LifeTales.domain.socket.handler.SearchWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final FindSocketService findSocketService;

    @Autowired
    public WebSocketConfig(FindSocketService findSocketService) {
        this.findSocketService = findSocketService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // WebSocket 핸들러 등록
        registry.addHandler(new FamilySearchWebSocketHandler(findSocketService), "/api/v1/users/basic/signUp/websocket-FamilySearch");
    }
}
