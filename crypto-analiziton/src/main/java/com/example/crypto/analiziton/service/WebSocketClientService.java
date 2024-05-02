package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.enums.CurrencyEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketClientService extends TextWebSocketHandler {

    private final RunStreamSubscribersService runStreamSubscribersService;
    private WebSocketSession currentSession;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private WebSocketConnectionManager connectionManager;
    private final ApplicationContext applicationContext;
    private final AtomicBoolean isReconnecting = new AtomicBoolean(false);
    private final AtomicInteger retryDelay = new AtomicInteger(0);
    private long checkTime;

    private WebSocketConnectionManager getConnectionManager() {
            connectionManager = Objects.requireNonNull(applicationContext).getBean(WebSocketConnectionManager.class);
        return connectionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        this.currentSession = session;
        log.info("Session established: " + session.getId());
        for (CurrencyEnum currency : CurrencyEnum.values()) {
            if (session.isOpen()) {
                log.info("Session : " + session.getId() + " send message.");
                session.sendMessage(runStreamSubscribersService.createTaskForCurrency(currency));
            }
        }
        startPingCycle();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        this.currentSession = session;
        checkTime = System.currentTimeMillis();
        log.info("Session : " + session.getId() + " receive message.");
        runStreamSubscribersService.handleMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status){
        log.info("Connection closed: " + session.getId() + ". Status: " + status);
        this.currentSession = null;
        if (!status.equals(CloseStatus.NORMAL) && !isReconnecting.get()) {
            reconnectToNewSession();
        }
        else if(status.equals(CloseStatus.NORMAL)){
            runStreamSubscribersService.shutdown();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, @NotNull Throwable exception) {
        log.error("Transport error in WebSocket session with ID: " + session.getId(), exception);
        reconnectToNewSession();
    }

    private void startPingCycle() {
        scheduler.scheduleAtFixedRate(this::sendPingAndCheckConnection, 0, 2, TimeUnit.SECONDS);
    }

    private void sendPingAndCheckConnection() {
        if ((System.currentTimeMillis() - checkTime) > 10000) {
            sendPing();
        }
    }

    private synchronized void reconnectToNewSession() {
        int i = 0;
        if (isReconnecting.compareAndSet(false, true)) {
            try {
                if (currentSession != null && currentSession.isOpen()) {
                    log.info("Force session closure for reconnect.");
                    currentSession.close();
                    currentSession = null;
                }
                do {
                    int delay = retryDelay.getAndUpdate(x -> x == 0 ? 10 : x * 2);
                    if (delay > 0) {
                        log.info("Waiting " + delay + " seconds before reconnecting...");
                    }
                    TimeUnit.SECONDS.sleep(delay);

                    log.info("Attempting to reconnect...");
                    if(connectionManager == null){
                        connectionManager = getConnectionManager();
                    }
                    if (connectionManager != null) {
                        log.info("Stopping existing connection manager...");
                        connectionManager.stop();
                        log.info("Re-starting connection manager...");
                        connectionManager.start();
                        log.info("Reconnection attempt has been made.");
                        boolean connected = waitForConnectionEstablished();
                        if (connected) {
                            log.info("Successfully reconnected.");
                            retryDelay.set(0);
                            break;
                        }
                    } else {
                        log.warn("Connection manager is null, cannot reconnect.");
                    }
                    log.warn("Failed to reconnect, was retry " + i++ + " time(s).");
                } while (true);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                log.error("Reconnection attempt was interrupted", ie);
            } catch (Exception e) {
                log.error("Error attempting to reconnect: ", e);
            } finally {
                isReconnecting.set(false);
                if (retryDelay.get() == 0) {
                    retryDelay.set(10);
                }
            }
        } else {
            log.info("Reconnection attempt skipped as another attempt is in progress");
        }
    }

    private void sendPing() {
        if (currentSession != null && currentSession.isOpen()) {
            try {
                currentSession.sendMessage(new PingMessage());
                log.info("Ping message sent to keep the connection alive.");
            } catch (IOException e) {
                log.error("Error sending ping message", e);
            }
        }
    }

     private synchronized boolean waitForConnectionEstablished() throws InterruptedException {
        log.info("Check new connection.");
        int timeout = 5;
        int checkInterval = 1;
        while (timeout > 0) {
            if (currentSession != null && currentSession.isOpen()) {
                log.info("New connection established.");
                return true;
            }
            TimeUnit.SECONDS.sleep(checkInterval);
            timeout -= checkInterval;
        }
        return false;
    }
}
