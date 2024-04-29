package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.component.CheckEmptyFieldCurrencyEntityComponent;
import com.example.crypto.analiziton.component.ParseJSONCurrencyComponent;
import com.example.crypto.analiziton.component.TickProcessingRunnable;
import com.example.crypto.analiziton.enums.CurrencyEnum;
import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.*;

@Service
@Slf4j
public class RunStreamSubscribersService {

    private ExecutorService executor;
    private final ParseJSONCurrencyComponent parseJSONCurrencyComponent;
    private Map<String, TickProcessingRunnable> runnableMap = new ConcurrentHashMap<>();
    private final TickAccumulatorService tickAccumulatorService;
    private final CheckEmptyFieldCurrencyEntityComponent checkEmptyFieldCurrencyEntityComponent;

    public RunStreamSubscribersService(ExecutorService executor,
                                       TickAccumulatorService tickAccumulatorService,
                                       CheckEmptyFieldCurrencyEntityComponent checkEmptyFieldCurrencyEntityComponent,
                                       ParseJSONCurrencyComponent parseJSONCurrencyComponent) {
        this.executor = executor;
        this.parseJSONCurrencyComponent = parseJSONCurrencyComponent;
        this.tickAccumulatorService = tickAccumulatorService;
        this.checkEmptyFieldCurrencyEntityComponent = checkEmptyFieldCurrencyEntityComponent;
        initializeRunnables();
    }

    public TextMessage createTaskForCurrency(CurrencyEnum currency) {
        try {
            String subscriptionMessage = getSubscriptionMessageForCurrency(currency);
            return new TextMessage(subscriptionMessage);
        } catch (Exception e) {
            log.error("Failed to queue message for currency: " + currency, e);
        }
        return null;
    }

    public void handleMessage(WebSocketSession session, TextMessage message) {
        try {
            if (message.getPayload().contains("\"success\":true")) {
                log.info("Confirmation of successful registration to receive data received.");
            } else {
                log.info("Received: " + message.getPayload());
                CurrencyEntity currencyEntity = parseJSONCurrencyComponent.parseJson(message);
                runnableMap.get(currencyEntity.getCurrencyName()).putCurrencyEntity(currencyEntity);
            }
        } catch (Exception e) {
            log.warn("Error processing message: " + e.getMessage());
        }
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

    public void restartSession() {
        log.info("Restart session...");
        shutdown();
        executor = Executors.newCachedThreadPool();
        runnableMap.clear();
        initializeRunnables();
    }

    private void initializeRunnables() {
        log.info("Initializing runnables for each currency...");
        for (CurrencyEnum currency : CurrencyEnum.values()) {
            runnableMap.put(currency.getSymbol(),
                    new TickProcessingRunnable(tickAccumulatorService,
                            checkEmptyFieldCurrencyEntityComponent));
        }
        runnableMap.values().forEach(executor::submit);
    }

    private String getSubscriptionMessageForCurrency(CurrencyEnum currency) {
        return String.format("{\"op\":\"subscribe\",\"args\":[\"tickers.%s\"]}", currency.getSymbol());
    }
}
