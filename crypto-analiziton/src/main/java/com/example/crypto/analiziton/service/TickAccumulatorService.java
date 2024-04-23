package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.component.CheckEmptyFieldCurrencyEntityComponent;
import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.example.crypto.analiziton.thread_detail.ThreadDetailsFormatter.logFormattedThreadDetails;

@Slf4j
@Data
@Service
@EnableScheduling
public class TickAccumulatorService {

    private final CheckEmptyFieldCurrencyEntityComponent emptyFieldCurrencyService;
    private BlockingQueue<CurrencyEntity> ticks = new LinkedBlockingQueue<>();
    private final CurrencyManipulationInDBService manipulationInDBService;

    @Value("${schedule.delay}")
    private int delay;

    @Value("${schedule.period}")
    private int period;

    public TickAccumulatorService(CheckEmptyFieldCurrencyEntityComponent emptyFieldCurrencyService,
                                  CurrencyManipulationInDBService manipulationInDBService) {
        this.emptyFieldCurrencyService = emptyFieldCurrencyService;
        this.manipulationInDBService = manipulationInDBService;
    }

    @Scheduled(fixedDelayString = "${schedule.delay}", initialDelayString = "${schedule.period}")
    public void processTicks() {
        List<CurrencyEntity> ticksForRecord = new ArrayList<>();
        log.info("Before record in new collection: " + ticks.size());
        ticks.drainTo(ticksForRecord);
        log.info("After record in new collection: " + ticks.size());
        log.info("Before record in DB: " + ticksForRecord.size());
        if (!ticksForRecord.isEmpty()) {
            manipulationInDBService.saveCollectionCurrencyEntityInDB(ticksForRecord);
        }
//        logFormattedThreadDetails();
    }

    public void addTick(CurrencyEntity tick) {
        if (emptyFieldCurrencyService.checkFullFieldsBeforeRecordInBD(tick)) {
            ticks.add(tick);
        }
    }
}
