package com.example.crypto.analiziton.converter;

import com.example.crypto.analiziton.dto.BaseStatisticData;
import com.example.crypto.analiziton.helper.CalculationOfDataByTicks;
import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConverterFromCurrencyEntityToBaseStatisticData {

    private final CalculationOfDataByTicks calculationOfDataByTicks;

    public BaseStatisticData converting(List<CurrencyEntity> currencyEntityList,
                                        Timestamp startTimestamp,
                                        Timestamp stopTimestamp) {
        BaseStatisticData baseStatisticData = new BaseStatisticData();
        baseStatisticData.setCurrencyName(currencyEntityList.getFirst().getCurrencyName());
        baseStatisticData.setStartTime(startTimestamp);
        baseStatisticData.setStopTime(stopTimestamp);
        baseStatisticData.setNeutralTicks(calculationOfDataByTicks.receiveNeutralTicks(currencyEntityList));
        baseStatisticData.setAscendingTicks(calculationOfDataByTicks.receiveAscendingTicks(currencyEntityList));
        baseStatisticData.setDescendingTicks(calculationOfDataByTicks.receiveDescendingTicks(currencyEntityList));
        baseStatisticData.setAllCountingTicks(calculationOfDataByTicks.receiveAllCountingTicks(currencyEntityList));
        return baseStatisticData;
    }
}
