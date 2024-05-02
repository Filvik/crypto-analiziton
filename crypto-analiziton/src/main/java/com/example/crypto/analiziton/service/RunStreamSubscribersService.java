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

    private final ExecutorService executor;
    private final ParseJSONCurrencyComponent parseJSONCurrencyComponent;
    private Map<String, TickProcessingRunnable> runnableMap = new ConcurrentHashMap<>();

    public RunStreamSubscribersService(ExecutorService executor,
                                       TickAccumulatorService tickAccumulatorService,
                                       CheckEmptyFieldCurrencyEntityComponent checkEmptyFieldCurrencyEntityComponent,
                                       ParseJSONCurrencyComponent parseJSONCurrencyComponent) {
        this.executor = executor;
        this.parseJSONCurrencyComponent = parseJSONCurrencyComponent;

        for (CurrencyEnum currency : CurrencyEnum.values()) {
            runnableMap.put(currency.getSymbol(),
                    new TickProcessingRunnable(tickAccumulatorService, checkEmptyFieldCurrencyEntityComponent));
        }
        runnableMap.values().forEach(executor::submit);
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
                //log.info("Received: " + message.getPayload());
                CurrencyEntity currencyEntity = parseJSONCurrencyComponent.parseJson(message);
                runnableMap.get(currencyEntity.getCurrencyName()).putCurrencyEntity(currencyEntity);
            }
        } catch (Exception e) {
            log.warn("Error processing message: " + e.getMessage());
        }
    }

    public void shutdown() {
        runnableMap.values().forEach(TickProcessingRunnable::stopRunning);
        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    log.error("Executor did not terminate");
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }


    private String getSubscriptionMessageForCurrency(CurrencyEnum currency) {
        return String.format("{\"op\":\"subscribe\",\"args\":[\"tickers.%s\"]}", currency.getSymbol());
    }
}
