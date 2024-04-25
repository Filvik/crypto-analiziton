package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.model.CurrencyEntity;
import com.example.crypto.analiziton.repository.CurrencyRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Data
@Slf4j
@RequiredArgsConstructor
public class CurrencyManipulationInDBService {

    private final CurrencyRepository currencyRepository;

    @Transactional
    public void saveCollectionCurrencyEntityInDB(List<CurrencyEntity> ticks) {
        if (!ticks.isEmpty()) {
            long startRecord = System.currentTimeMillis();
            for (CurrencyEntity tick : ticks) {
                try {
                    currencyRepository.save(tick);
                } catch (Exception e) {
                    log.warn("Error saving ticks:" + tick.getCurrencyName() + ". Time: " + tick.getCreatedAt());
                }
            }
            long stopRecord = System.currentTimeMillis();
            currencyRepository.flush();
            log.info("Size collection: " + ticks.size());
            log.info("Recording time: " + (stopRecord - startRecord));
        }
    }

    @Transactional(readOnly = true)
    public List<CurrencyEntity> receiveCurrencyEntity(String currencyName,
                                                      Timestamp startTimestamp,
                                                      Timestamp stopTimestamp) {
        return currencyRepository.findAllByCurrencyNameAndPeriodOfTime
                (currencyName, startTimestamp, stopTimestamp);
    }
}
