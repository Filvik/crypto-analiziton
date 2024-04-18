package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.exeption.ValidCurrencyEntityException;
import com.example.crypto.analiziton.helper.TimestampAdjuster;
import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class TickProcessingService {

    private final TickAccumulatorService tickAccumulator;
    private final CheckEmptyFieldCurrencyEntityService emptyFieldCurrencyService;
    private CurrencyEntity lastCurrencyEntity;


    public void processIncomingTick(CurrencyEntity tick) {
        CurrencyEntity currencyEntityForRecord;
        if (emptyFieldCurrencyService.checkBaseFields(lastCurrencyEntity)) {
            currencyEntityForRecord = updateCurrencyEntityForRecord(lastCurrencyEntity, tick);
        } else {
            log.warn("The last record has empty field(s).");
            currencyEntityForRecord = tick;
        }
        lastCurrencyEntity = currencyEntityForRecord;
        tickAccumulator.addTick(currencyEntityForRecord);
    }

    private CurrencyEntity updateCurrencyEntityForRecord(CurrencyEntity lastCurrencyEntity, CurrencyEntity tick) {

        CurrencyEntity currencyEntityForRecord = new CurrencyEntity();
        try {
            // Base block
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

            if (!Objects.equals(tick.getCreatedAt(), null)) {
                currencyEntityForRecord.setCreatedAt(tick.getCreatedAt());
            } else {
                currencyEntityForRecord.setCreatedAt(TimestampAdjuster.addMillisecondsToTimestamp(lastCurrencyEntity.getCreatedAt(), 100));
            }

            // Extra block
            if (tick.getBidPrice() != 0) {
                currencyEntityForRecord.setBidPrice(tick.getBidPrice());
            } else {
                if (lastCurrencyEntity.getBidPrice() != 0) {
                    currencyEntityForRecord.setBidPrice(lastCurrencyEntity.getBidPrice());
                } else {
                    currencyEntityForRecord.setBidPrice(0.01);
                }
            }

            if (tick.getBidSize() != 0) {
                currencyEntityForRecord.setBidSize(tick.getBidSize());
            } else {
                if (lastCurrencyEntity.getBidSize() != 0) {
                    currencyEntityForRecord.setBidSize(lastCurrencyEntity.getBidSize());
                } else {
                    currencyEntityForRecord.setBidSize(0.01);
                }
            }

            if (tick.getAskPrice() != 0) {
                currencyEntityForRecord.setAskPrice(tick.getAskPrice());
            } else {
                if (lastCurrencyEntity.getAskPrice() != 0) {
                    currencyEntityForRecord.setAskPrice(lastCurrencyEntity.getAskPrice());
                } else {
                    currencyEntityForRecord.setAskPrice(0.01);
                }
            }

            if (tick.getAskSize() != 0) {
                currencyEntityForRecord.setAskSize(tick.getAskSize());
            } else {
                if (lastCurrencyEntity.getAskSize() != 0) {
                    currencyEntityForRecord.setAskSize(lastCurrencyEntity.getAskSize());
                } else {
                    currencyEntityForRecord.setAskSize(0.01);
                }
            }

        } catch (Exception exception) {
            log.warn("CurrencyEntity from BD failed validation.");
            throw new ValidCurrencyEntityException("CurrencyEntity from BD failed validation.");
        }
        return currencyEntityForRecord;
    }
}

