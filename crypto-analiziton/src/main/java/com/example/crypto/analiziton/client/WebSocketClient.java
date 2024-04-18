package com.example.crypto.analiziton.client;

import com.example.crypto.analiziton.service.ParseJSONCurrencyService;
import com.example.crypto.analiziton.service.TickProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketClient extends TextWebSocketHandler {

    private final TickProcessingService tickProcessingService;
    private final ParseJSONCurrencyService parseJSONCurrencyService;

    private final String currency = "{\"op\":\"subscribe\",\"args\":[\"tickers.BTCUSDT\"]}";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage(currency));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            System.out.println("Received: " + message.getPayload());
            tickProcessingService.processIncomingTick(parseJSONCurrencyService.parseJson(message));
        }
      catch (Exception e){
            log.warn("Error record in BD: " + e);
      }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Connection closed. Status: " + status);
    }
}
