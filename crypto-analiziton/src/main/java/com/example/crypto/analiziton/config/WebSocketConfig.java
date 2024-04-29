package com.example.crypto.analiziton.config;

import com.example.crypto.analiziton.service.WebSocketClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketClientService webSocketClientService;
    @Value("${bybit.ws.url}")
    private String bybitWsUrl;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketClientService, "/ws/bybit").setAllowedOrigins("*");
    }

    @Bean
    public WebSocketConnectionManager webSocketConnectionManager(WebSocketClientService webSocketClientService) {
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client, webSocketClientService, bybitWsUrl);
        manager.setAutoStartup(true);
        return manager;
    }
}

