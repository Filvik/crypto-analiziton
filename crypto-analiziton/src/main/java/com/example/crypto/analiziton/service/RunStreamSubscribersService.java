package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.enums.CurrencyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class RunStreamSubscribersService {

    private final ExecutorService executor;
    private final Map<CurrencyEnum, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public RunStreamSubscribersService(ExecutorService executor) {
        this.executor = executor;
    }

    public TextMessage createTaskForCurrency(CurrencyEnum currency) {
            try {
                String subscriptionMessage = getSubscriptionMessageForCurrency(currency);
                return new TextMessage(subscriptionMessage);
            } catch (Exception e) {
                log.error("Failed to queue message for currency " + currency, e);
            }
        return null;
    }

    public void removeSession(WebSocketSession session) {
        sessions.values().remove(session);
    }

    public void shutdown() {
        try {
            log.info("Shutting down executor service and message processor...");
            executor.shutdown();
            if (!executor.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            log.error("Failed to shut down executor service.", e);
            executor.shutdownNow();
        }
    }

    public String getSubscriptionMessageForCurrency(CurrencyEnum currency) {
        return String.format("{\"op\":\"subscribe\",\"args\":[\"tickers.%s\"]}", currency.getSymbol());
    }
}
