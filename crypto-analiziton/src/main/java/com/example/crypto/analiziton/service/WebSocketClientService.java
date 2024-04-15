package com.example.crypto.analiziton.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

@Service
public class WebSocketClientService {

    @Value("${bybit.ws.url}")
    private String bybitWsUrl;

    private WebSocketConnectionManager connectionManager;

    @PostConstruct
    public void initializeWebSocketConnection() {
        StandardWebSocketClient client = new StandardWebSocketClient();
        this.connectionManager = new WebSocketConnectionManager(client, new TextWebSocketHandler() {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                String subscribeMessage = "{\"op\":\"subscribe\",\"args\":[\"tickers.BTCUSDT\"]}";
                session.sendMessage(new TextMessage(subscribeMessage));
                System.out.println("Subscribed to BTCUSDT ticker");
            }

            @Override
            protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
                System.out.println("Received: " + message.getPayload());
            }
        }, this.bybitWsUrl);

        this.connectionManager.start();
    }
}