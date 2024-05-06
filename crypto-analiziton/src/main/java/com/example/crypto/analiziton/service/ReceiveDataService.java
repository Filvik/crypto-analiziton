package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.converter.ConverterFromCurrencyEntityToBaseStatisticData;
import com.example.crypto.analiziton.converter.ConverterFromCurrencyEntityToFullStatisticData;
import com.example.crypto.analiziton.dto.FullDetailsStatisticData;
import com.example.crypto.analiziton.dto.BaseStatisticData;
import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceiveDataService {

    private final CurrencyManipulationInDBService currencyManipulationInDBService;
    private final ConverterFromCurrencyEntityToBaseStatisticData converterFromCurrencyEntityToBaseStatisticData;
    private final ConverterFromCurrencyEntityToFullStatisticData converterFromCurrencyEntityToFullStatisticData;

    public BaseStatisticData receiveBaseDataFromDB(String currencyName, Long dateStart, Long dateStop) {
        BaseStatisticData baseStatisticData = new BaseStatisticData();
        Timestamp startTimestamp = new Timestamp(dateStart);
        Timestamp stopTimestamp = new Timestamp(dateStop);
        List<CurrencyEntity> currencyEntityList = currencyManipulationInDBService
                .receiveCurrencyEntity(currencyName, startTimestamp, stopTimestamp);
        if (!currencyEntityList.isEmpty()) {
            baseStatisticData = converterFromCurrencyEntityToBaseStatisticData
                    .converting(currencyEntityList, startTimestamp, stopTimestamp);
        }

        return baseStatisticData;
    }

    public List<FullDetailsStatisticData> receiveCollectionDateFromDB(String currencyName, Long dateStart, Long dateStop) {
        List<FullDetailsStatisticData> fullDetailsStatisticData = new ArrayList<>();
        Timestamp startTimestamp = new Timestamp(dateStart);
        Timestamp stopTimestamp = new Timestamp(dateStop);
        List<CurrencyEntity> currencyEntityList = currencyManipulationInDBService
                .receiveCurrencyEntity(currencyName, startTimestamp, stopTimestamp);

        if (!currencyEntityList.isEmpty()) {
            fullDetailsStatisticData = converterFromCurrencyEntityToFullStatisticData.converting(currencyEntityList);
        }

        return fullDetailsStatisticData;
    }
}
