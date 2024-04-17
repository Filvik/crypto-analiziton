package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.exeption.ValidCurrencyEntityException;
import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TickProcessingService {

    private final TickAccumulatorService tickAccumulator;
    private final CheckEmptyFieldCurrencyEntityService emptyFieldCurrencyService;
    private CurrencyEntity lastCurrencyEntity;


    public void processIncomingTick(CurrencyEntity tick) {
        CurrencyEntity currencyEntityForRecord;
        if (lastCurrencyEntity != null) {
            currencyEntityForRecord = updateCurrencyEntityForRecord(lastCurrencyEntity, tick);
        } else {
            currencyEntityForRecord = tick;
        }
        lastCurrencyEntity = currencyEntityForRecord;
        tickAccumulator.addTick(currencyEntityForRecord);
    }

    private CurrencyEntity updateCurrencyEntityForRecord(CurrencyEntity lastCurrencyEntity, CurrencyEntity tick) {

        CurrencyEntity currencyEntityForRecord = new CurrencyEntity();
        try {
            if (emptyFieldCurrencyService.check(lastCurrencyEntity)) {

                if (tick.getCurrencyName() != null && !tick.getCurrencyName().isEmpty()) {
                    currencyEntityForRecord.setCurrencyName(tick.getCurrencyName());
                }

                if (tick.getTickDirection() != null && !tick.getTickDirection().isEmpty()) {
                    currencyEntityForRecord.setTickDirection(tick.getTickDirection());
                } else {
                    currencyEntityForRecord.setTickDirection(lastCurrencyEntity.getTickDirection());
                }

                if (tick.getPrice() != 0) {
                    currencyEntityForRecord.setPrice(tick.getPrice());
                } else {
                    currencyEntityForRecord.setPrice(lastCurrencyEntity.getPrice());
                }

                if (tick.getBidPrice() != 0) {
                    currencyEntityForRecord.setBidPrice(tick.getBidPrice());
                } else {
                    currencyEntityForRecord.setBidPrice(lastCurrencyEntity.getBidPrice());
                }

                if (tick.getBidSize() != 0) {
                    currencyEntityForRecord.setBidSize(tick.getBidSize());
                } else {
                    currencyEntityForRecord.setBidSize(lastCurrencyEntity.getBidSize());
                }

                if (tick.getAskPrice() != 0) {
                    currencyEntityForRecord.setAskPrice(tick.getAskPrice());
                } else {
                    currencyEntityForRecord.setAskPrice(lastCurrencyEntity.getAskPrice());
                }

                if (tick.getAskSize() != 0) {
                    currencyEntityForRecord.setAskSize(tick.getAskSize());
                } else {
                    currencyEntityForRecord.setAskSize(lastCurrencyEntity.getAskSize());
                }

                if (!tick.getCreatedAt().equals(null)) {
                    currencyEntityForRecord.setCreatedAt(tick.getCreatedAt());
                } else {
                    currencyEntityForRecord.setCreatedAt(lastCurrencyEntity.getCreatedAt());
                }

            } else {
                log.warn("Error when fill in new data.");
                throw new RuntimeException("Error when fill in new data.");
            }
        } catch (Exception exception) {
            log.warn("CurrencyEntity from BD failed validation.");
            throw new ValidCurrencyEntityException("CurrencyEntity from BD failed validation.");
        }
        return currencyEntityForRecord;
    }
}

