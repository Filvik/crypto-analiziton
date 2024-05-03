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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class WebSocketClientService extends TextWebSocketHandler {

    private final RunStreamSubscribersService runStreamSubscribersService;
    private WebSocketSession currentSession;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private WebSocketConnectionManager connectionManager;
    private final ApplicationContext applicationContext;
    private final AtomicBoolean isReconnecting = new AtomicBoolean(false);
    private final AtomicInteger retryDelay = new AtomicInteger(0);
    private long checkTime;

    /**
     * Вызывается после установления соединения с WebSocket.
     *
     * @param session сессия, которая была установлена.
     * @throws IOException исключение при ошибке ввода/вывода.
     */
    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) throws IOException {
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

    /**
     * Обрабатывает текстовые сообщения, полученные от клиента.
     *
     * @param session сессия, от которой было получено сообщение.
     * @param message текстовое сообщение.
     */
    @Override
    protected void handleTextMessage(@NotNull WebSocketSession session, @NotNull TextMessage message) {
        this.currentSession = session;
        checkTime = System.currentTimeMillis();
        runStreamSubscribersService.handleMessage(session, message);
    }

    /**
     * Вызывается после закрытия соединения WebSocket.
     *
     * @param session сессия, которая была закрыта.
     * @param status  статус закрытия.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, @NotNull CloseStatus status) throws IOException {
        log.info("Connection closed: " + session.getId() + ". Status: " + status);
        this.currentSession = null;
        if (!status.equals(CloseStatus.NORMAL) && !isReconnecting.get()) {
            reconnectToNewSession();
        } else if (status.equals(CloseStatus.NORMAL)) {
            runStreamSubscribersService.shutdown();
        }
    }

    /**
     * Обрабатывает ошибки транспорта во время сессии WebSocket.
     *
     * @param session   сессия, в которой произошла ошибка.
     * @param exception исключение, представляющее ошибку.
     */
    @Override
    public void handleTransportError(WebSocketSession session, @NotNull Throwable exception) {
        log.error("Transport error in WebSocket session with ID: " + session.getId(), exception);
        reconnectToNewSession();
    }

    /**
     * Инициирует периодическую отправку Ping-сообщений для поддержания активности соединения.
     */
    private void startPingCycle() {
        scheduler.scheduleAtFixedRate(this::sendPingAndCheckConnection, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Отправляет Ping-сообщение, если соединение активно более 10 секунд без получения сообщений.
     */
    private void sendPingAndCheckConnection() {
        if ((System.currentTimeMillis() - checkTime) > 10000) {
            sendPing();
        }
    }

    /**
     * Инициирует процесс переподключения, если это возможно.
     */
    private void reconnectToNewSession() {
        if (isReconnecting.compareAndSet(false, true)) {
            performReconnect();
        } else {
            log.info("Reconnection attempt skipped as another attempt is in progress");
        }
    }

    /**
     * Выполняет шаги для переподключения.
     */
    private void performReconnect() {
        try {
            if (currentSession != null && currentSession.isOpen()) {
                log.info("Force session closure for reconnect.");
                currentSession.close();
                currentSession = null;
            }
            log.info("Attempting to reconnect...");
            if (connectionManager == null) {
                connectionManager = getConnectionManager();
            }
            connectionManager.stop();
            if (checkInternetConnection()) {
                connectionManager.start();
            }
            log.info("Reconnection attempt has been made.");
            waitForConnectionEstablished(5);
        } catch (Exception e) {
            log.error("Error attempting to reconnect: ", e);
            scheduleReconnect();
        }
    }

    /**
     * Планирует повторную попытку переподключения.
     */
    private void scheduleReconnect() {
        int delay = retryDelay.getAndUpdate(x -> x == 0 ? 10 : x * 2);
        log.info("Scheduled reconnection in " + delay + " seconds.");
        scheduler.schedule(this::performReconnect, delay, TimeUnit.SECONDS);
    }

    /**
     * Проверяет состояние установленного соединения.
     *
     * @param timeout тайм-аут в секундах для установления соединения.
     */
    private void waitForConnectionEstablished(int timeout) {
        scheduler.schedule(() -> {
            if (currentSession != null && currentSession.isOpen()) {
                log.info("Successfully reconnected.");
                retryDelay.set(0);
                isReconnecting.set(false);
            } else if (timeout > 0) {
                waitForConnectionEstablished(timeout - 1);
            } else {
                log.info("Failed to establish connection, retrying...");
                scheduleReconnect();
            }
        }, 1, TimeUnit.SECONDS);
    }

    /**
     * Отправляет Ping-сообщение для поддержания активности соединения.
     */
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

    /**
     * Возвращает менеджер подключений WebSocket.
     *
     * @return экземпляр {@link WebSocketConnectionManager}.
     */
    private WebSocketConnectionManager getConnectionManager() {
        connectionManager = Objects.requireNonNull(applicationContext).getBean(WebSocketConnectionManager.class);
        return connectionManager;
    }

    /**
     * Проверяет наличие интернет-соединения путем проверки доступности ресурса по заданному URL.
     *
     * @return true, если интернет-соединение доступно, false в противном случае.
     */
    private boolean checkInternetConnection() {
        try {
            String testUrl = "http://www.google.com";
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(testUrl))
                    .method("HEAD", HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode() >= 200 && response.statusCode() <= 399;
        } catch (Exception e) {
            log.warn("There isn't internet connection.");
            return false;
        }
    }
}
