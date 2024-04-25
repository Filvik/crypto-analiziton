package com.example.crypto.analiziton.helper;

import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CalculationOfDataByTicks {

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
}
