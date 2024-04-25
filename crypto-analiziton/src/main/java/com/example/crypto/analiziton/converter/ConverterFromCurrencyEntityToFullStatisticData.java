package com.example.crypto.analiziton.converter;

import com.example.crypto.analiziton.dto.FullStatisticData;
import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ConverterFromCurrencyEntityToFullStatisticData {


    public List<FullStatisticData> converting(List<CurrencyEntity> currencyEntityList) {
        return currencyEntityList.stream()
                .map(this::converting)
                .collect(Collectors.toList());
    }

    public FullStatisticData converting(CurrencyEntity currencyEntity){
        FullStatisticData fullStatisticData = new FullStatisticData();
        fullStatisticData.setCurrencyName(currencyEntity.getCurrencyName());
        fullStatisticData.setPrice(currencyEntity.getPrice());
        fullStatisticData.setTickDirection(currencyEntity.getTickDirection());
        fullStatisticData.setCreatedAt(currencyEntity.getCreatedAt());
        return fullStatisticData;
    }
}
