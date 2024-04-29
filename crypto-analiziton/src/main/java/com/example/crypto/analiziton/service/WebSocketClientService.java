package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.enums.CurrencyEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketClientService extends TextWebSocketHandler {

    private final RunStreamSubscribersService runStreamSubscribersService;
    private final ScheduledExecutorService timeoutExecutor = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> timeoutTask;
    private WebSocketConnectionManager connectionManager;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        log.info("WebSocket session established with ID: " + session.getId());
        for (CurrencyEnum currency : CurrencyEnum.values()) {
            session.sendMessage(runStreamSubscribersService.createTaskForCurrency(currency));
        }
        resetTimeout(session);
    }

    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) {
        runStreamSubscribersService.handleMessage(session, message);
        resetTimeout(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) throws IOException {
        log.info("WebSocket session closed with ID: " + session.getId() + "; Close status: " + status);
        runStreamSubscribersService.shutdown();
        if (timeoutTask != null) {
            timeoutTask.cancel(false);
        }
        if (status.getCode() != CloseStatus.NORMAL.getCode()) {
            reconnect();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, @NotNull Throwable exception) throws Exception {
        log.error("Transport error in WebSocket session with ID: " + session.getId(), exception);
        runStreamSubscribersService.shutdown();
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    private void resetTimeout(WebSocketSession session) {
        if (timeoutTask != null && !timeoutTask.isDone()) {
            timeoutTask.cancel(false);
        }
        timeoutTask = timeoutExecutor.schedule(() -> {
            try {
                log.warn("No message received for 10 seconds. Closing current session and attempting to reconnect.");
                if (session.isOpen()) {
                    session.close(CloseStatus.SESSION_NOT_RELIABLE);
                }
                reconnect();
            } catch (Exception e) {
                log.error("Failed to close session or reconnect", e);
            }
        }, 10, TimeUnit.SECONDS);
    }

    private void reconnect() {
        if (connectionManager != null) {
            log.info("Attempting to create a new connection session...");
            try {
                connectionManager.stop();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupted while waiting to reconnect", e);
                return;
            }

            StandardWebSocketClient webSocketClient = new StandardWebSocketClient();
            connectionManager = new WebSocketConnectionManager(webSocketClient, this, "wss://stream.bybit.com/contract/usdt/public/v3");
            connectionManager.setAutoStartup(false);
            try {
                connectionManager.start();
            } catch (Exception e) {
                log.error("Failed to reconnect", e);
            }
        }
    }

}
