package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.model.CurrencyEntity;
import com.example.crypto.analiziton.repository.CurrencyRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            try {
                long start = System.currentTimeMillis();
                currencyRepository.saveAllAndFlush(ticks);
                long stop = System.currentTimeMillis();
                log.info("Size collection: " + ticks.size());
                log.info("Recording time: " + (stop - start));
            } catch (Exception e) {
                log.error("Error saving ticks to DB", e);
            }
        }
    }


//    @Transactional
//    public synchronized void saveCollectionCurrencyEntityInDB(List<CurrencyEntity> ticks) {
//        if (!ticks.isEmpty()) {
//            long start = System.currentTimeMillis();
//            for (CurrencyEntity tick : ticks) {
//                currencyRepository.save(tick);
//            }
//            long stop = System.currentTimeMillis();
//            currencyRepository.flush();
//            System.out.println(ticks.size());
//            System.out.println(stop - start);
//        }
//    }

}
