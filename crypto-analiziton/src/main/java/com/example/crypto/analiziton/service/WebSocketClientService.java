package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.enums.CurrencyEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketClientService extends TextWebSocketHandler {

    private final RunStreamSubscribersService runStreamSubscribersService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        log.info("Session: " + session.getId());
        for (CurrencyEnum currency : CurrencyEnum.values()) {
            session.sendMessage(runStreamSubscribersService.createTaskForCurrency(currency));
        }
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) {
        runStreamSubscribersService.handleMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) throws IOException {
        log.info("Connection closed: " + session.getId() + ". Status: " + status);
        runStreamSubscribersService.shutdown();
        session.close();
    }
}
