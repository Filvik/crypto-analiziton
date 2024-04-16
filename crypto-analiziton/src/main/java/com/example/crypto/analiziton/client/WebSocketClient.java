package com.example.crypto.analiziton.client;

import com.example.crypto.analiziton.service.CurrencyManipulationInDB;
import com.example.crypto.analiziton.service.ParseJSONCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class WebSocketClient extends TextWebSocketHandler {

    private final CurrencyManipulationInDB currencyManipulationInDB;
    private final ParseJSONCurrencyService parseJSONCurrencyService;

    private final String currency = "{\"op\":\"subscribe\",\"args\":[\"tickers.BTCUSDT\"]}";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage(currency));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        currencyManipulationInDB.saveCurrencyEntityInDB(parseJSONCurrencyService.parseJson(message));
        System.out.println("Received: " + message.getPayload());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("Connection closed. Status: " + status);
    }
}
