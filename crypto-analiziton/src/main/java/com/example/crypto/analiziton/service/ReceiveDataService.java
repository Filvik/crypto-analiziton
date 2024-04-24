package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.model.StatisticData;
import com.example.crypto.analiziton.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceiveDataService {

    private final CurrencyRepository currencyRepository;

    public StatisticData receiveDateFromDB(String currencyName, Long dataStart, Long dataStop) {
        StatisticData statisticData = new StatisticData();

        return statisticData;
    }
}
