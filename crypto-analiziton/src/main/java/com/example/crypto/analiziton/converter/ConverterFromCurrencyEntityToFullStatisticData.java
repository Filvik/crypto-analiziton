package com.example.crypto.analiziton.converter;

import com.example.crypto.analiziton.dto.FullDetailsStatisticData;
import com.example.crypto.analiziton.helper.ReceiveDataByTicks;
import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConverterFromCurrencyEntityToFullStatisticData {

    private final ReceiveDataByTicks receiveDataByTicks;

    public List<FullDetailsStatisticData> converting(List<CurrencyEntity> currencyEntityList) {
        return currencyEntityList.stream()
                .map(this::converting)
                .collect(Collectors.toList());
    }

    public FullDetailsStatisticData converting(CurrencyEntity currencyEntity){
        FullDetailsStatisticData fullDetailsStatisticData = new FullDetailsStatisticData();
        fullDetailsStatisticData.setCurrencyName(currencyEntity.getCurrencyName());
        fullDetailsStatisticData.setPrice(currencyEntity.getPrice());
        fullDetailsStatisticData.setVolume(currencyEntity.getVolume());
        fullDetailsStatisticData.setCreatedAt(currencyEntity.getCreatedAt().getTime());
        return fullDetailsStatisticData;
    }
}
