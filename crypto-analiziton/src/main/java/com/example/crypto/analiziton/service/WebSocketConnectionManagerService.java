package com.example.crypto.analiziton.service;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;


@Service
@RequiredArgsConstructor
@Data
public class WebSocketConnectionManagerService {

    @Value("${bybit.ws.url}")
    private String bybitWsUrl;

    private final WebSocketClientService webSocketClientService;


    @PostConstruct
    public void initializeWebSocketConnection() {
        WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(
                new StandardWebSocketClient(), webSocketClientService, bybitWsUrl
        );
        connectionManager.start();
    }
}
