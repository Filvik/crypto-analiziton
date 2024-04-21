package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.model.CurrencyEntity;
import com.example.crypto.analiziton.repository.CurrencyRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Data
@Service
public class TickAccumulatorService {

    private final CheckEmptyFieldCurrencyEntityService emptyFieldCurrencyService;
    private BlockingQueue<CurrencyEntity> ticks = new LinkedBlockingQueue<>();
    private final Timer timer = new Timer();
    private final CurrencyRepository currencyRepository;
    private int delay;
    private int period;

    public TickAccumulatorService(CurrencyRepository currencyRepository,
                                  CheckEmptyFieldCurrencyEntityService emptyFieldCurrencyService,
                                  @Value("${delay}") int delay,
                                  @Value("${period}") int period) {
        this.emptyFieldCurrencyService = emptyFieldCurrencyService;
        this.currencyRepository = currencyRepository;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                flushToDatabase();
                log.info("Active threads: " + Thread.activeCount());
            }
        }, delay, period);
    }

    public synchronized void addTick(CurrencyEntity tick) {
        if (emptyFieldCurrencyService.checkFullFieldsBeforeRecordInBD(tick)) {
            ticks.add(tick);
        }
    }

    private synchronized void flushToDatabase() {
        if (!ticks.isEmpty()) {
            currencyRepository.saveAllAndFlush(ticks);
            ticks.clear();
        }
    }
}

