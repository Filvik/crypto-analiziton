package com.example.crypto.analiziton.converter;

import com.example.crypto.analiziton.dto.FullStatisticData;
import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ConverterFromCurrencyEntityToFullStatisticData {


    public List<FullStatisticData> converting(List<CurrencyEntity> currencyEntityList) {
        List<FullStatisticData> fullStatisticData = new ArrayList<>();
        for (CurrencyEntity currencyEntity: currencyEntityList){
            FullStatisticData fullStatistic = converting(currencyEntity);
            fullStatisticData.add(fullStatistic);
        }
        return fullStatisticData;
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
