package com.example.crypto.analiziton.helper;

import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Component
public class ReceiveDataByTicks {

    public long receiveNeutralTicks(List<CurrencyEntity> currencyEntityList) {
        return currencyEntityList.stream()
                .filter(currency -> currency.getTickDirection().equals("ZeroMinusTick")
                        || currency.getTickDirection().equals("ZeroPlusTick"))
                .count();
    }

    public long receiveAscendingTicks(List<CurrencyEntity> currencyEntityList) {
        return currencyEntityList.stream()
                .filter(currency -> currency.getTickDirection().equals("PlusTick"))
                .count();
    }

    public long receiveDescendingTicks(List<CurrencyEntity> currencyEntityList) {
        return currencyEntityList.stream()
                .filter(currency -> currency.getTickDirection().equals("MinusTick"))
                .count();
    }

    public long receiveAllCountingTicks(List<CurrencyEntity> currencyEntityList) {
        return currencyEntityList.size();
    }


    public double receiveStartPrice(List<CurrencyEntity> currencyEntityList, Timestamp startTimestamp) {
        return currencyEntityList.stream()
                .min(Comparator.comparingLong(currency -> Math.abs(currency.getCreatedAt().getTime() - startTimestamp.getTime())))
                .map(CurrencyEntity::getPrice)
                .orElseThrow(() -> new IllegalArgumentException("No price found near the start timestamp"));
    }

    public double receiveStopPrice(List<CurrencyEntity> currencyEntityList, Timestamp stopTimestamp) {
        return currencyEntityList.stream()
                .min(Comparator.comparingLong(currency -> Math.abs(currency.getCreatedAt().getTime() - stopTimestamp.getTime())))
                .map(CurrencyEntity::getPrice)
                .orElseThrow(() -> new IllegalArgumentException("No price found near the stop timestamp"));
    }

    public double receiveMaxValue(List<CurrencyEntity> currencyEntityList) {
        return currencyEntityList.stream()
                .mapToDouble(CurrencyEntity::getPrice)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("Cannot compute maximum value on an empty list"));
    }

    public double receiveMinValue(List<CurrencyEntity> currencyEntityList) {
        return currencyEntityList.stream()
                .mapToDouble(CurrencyEntity::getPrice)
                .min()
                .orElseThrow(() -> new IllegalArgumentException("Cannot compute minimum value on an empty list"));
    }

    public String getDirection(String tickDirection) {
        if (tickDirection.equals("PlusTick")) {
            return "UP";
        } else if (tickDirection.equals("MinusTick")) {
            return "DOWN";
        } else {
            return "NEUTRAL";
        }
    }

}
