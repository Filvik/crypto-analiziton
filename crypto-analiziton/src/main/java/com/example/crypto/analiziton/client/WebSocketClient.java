package com.example.crypto.analiziton.client;

import com.example.crypto.analiziton.enums.CurrencyEnum;
import com.example.crypto.analiziton.model.CurrencyEntity;
import com.example.crypto.analiziton.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class WebSocketClient extends TextWebSocketHandler {

    private final ParseJSONCurrencyService parseJSONCurrencyService;
    private final RunStreamSubscribersService runStreamSubscribersService;
    private Map<String, TickProcessingRunnable> runnableMap = new ConcurrentHashMap<>();

    public WebSocketClient(ParseJSONCurrencyService parseJSONCurrencyService,
                           RunStreamSubscribersService runStreamSubscribersService,
                           TickAccumulatorService tickAccumulatorService,
                           CheckEmptyFieldCurrencyEntityService checkEmptyFieldCurrencyEntityService,
                           ExecutorService executor) {
        this.parseJSONCurrencyService = parseJSONCurrencyService;
        this.runStreamSubscribersService = runStreamSubscribersService;

        for (CurrencyEnum currency : CurrencyEnum.values()) {
            runnableMap.put(currency.getSymbol(),
                    new TickProcessingRunnable(tickAccumulatorService, checkEmptyFieldCurrencyEntityService));
        }
        runnableMap.values().forEach(executor::submit);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        for (CurrencyEnum currency : CurrencyEnum.values()) {
            session.sendMessage(runStreamSubscribersService.createTaskForCurrency(currency));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            if (message.getPayload().contains("\"success\":true")) {
                log.info("Confirmation of successful registration to receive data received.");
            } else {
                log.info("Received: " + message.getPayload());
                CurrencyEntity currencyEntity = parseJSONCurrencyService.parseJson(message);
                runnableMap.get(currencyEntity.getCurrencyName()).putCurrencyEntity(currencyEntity);
            }
        } catch (Exception e) {
            log.warn("Error processing message: " + e.getMessage(), e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info("Connection closed. Status: " + status);
        runStreamSubscribersService.removeSession(session);
        runStreamSubscribersService.shutdown();
    }
}
