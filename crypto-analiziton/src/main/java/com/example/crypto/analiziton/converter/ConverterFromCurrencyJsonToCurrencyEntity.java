package com.example.crypto.analiziton.converter;

import com.example.crypto.analiziton.model.CurrencyEntity;
import com.example.crypto.analiziton.model.CurrencyJson;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Component
public class ConverterFromCurrencyJsonToCurrencyEntity {

    public CurrencyEntity convertToEntity(CurrencyJson currencyJson) {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        CurrencyJson.DataCurrency dataCurrency = currencyJson.getDataCurrency();

        currencyEntity.setCurrencyName(dataCurrency.getSymbol());
        currencyEntity.setTickDirection(dataCurrency.getTickDirection());
        currencyEntity.setPrice(dataCurrency.getLastPrice());
        currencyEntity.setBidPrice(dataCurrency.getBidPrice());
        currencyEntity.setBidSize(dataCurrency.getBidSize());
        currencyEntity.setAskPrice(dataCurrency.getAskPrice());
        currencyEntity.setAskSize(dataCurrency.getAskSize());
        currencyEntity.setCreatedAt(Timestamp.from(Instant.ofEpochMilli(currencyJson.getTs())));

        return currencyEntity;
    }
}