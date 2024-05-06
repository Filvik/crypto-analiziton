package com.example.crypto.analiziton.converter;

import com.example.crypto.analiziton.dto.BaseStatisticData;
import com.example.crypto.analiziton.helper.ReceiveDataByTicks;
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

    private final ReceiveDataByTicks receiveDataByTicks;

    public BaseStatisticData converting(List<CurrencyEntity> currencyEntityList,
                                        Timestamp startTimestamp,
                                        Timestamp stopTimestamp) {
        BaseStatisticData baseStatisticData = new BaseStatisticData();
        baseStatisticData.setCurrencyName(currencyEntityList.get(0).getCurrencyName());
        baseStatisticData.setStartTime(startTimestamp);
        baseStatisticData.setStopTime(stopTimestamp);
        baseStatisticData.setNeutralTicks(receiveDataByTicks.receiveNeutralTicks(currencyEntityList));
        baseStatisticData.setAscendingTicks(receiveDataByTicks.receiveAscendingTicks(currencyEntityList));
        baseStatisticData.setDescendingTicks(receiveDataByTicks.receiveDescendingTicks(currencyEntityList));
        baseStatisticData.setAllCountingTicks(receiveDataByTicks.receiveAllCountingTicks(currencyEntityList));
        baseStatisticData.setPriceStart(receiveDataByTicks.receiveStartPrice(currencyEntityList, startTimestamp));
        baseStatisticData.setPriceStop(receiveDataByTicks.receiveStopPrice(currencyEntityList, stopTimestamp));
        baseStatisticData.setVolume(receiveDataByTicks.receiveSumVolume(currencyEntityList));
        baseStatisticData.setMaxValue(receiveDataByTicks.receiveMaxValue(currencyEntityList));
        baseStatisticData.setMinValue(receiveDataByTicks.receiveMinValue(currencyEntityList));
        return baseStatisticData;
    }
}
