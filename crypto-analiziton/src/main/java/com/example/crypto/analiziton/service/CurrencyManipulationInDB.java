package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.model.CurrencyEntity;
import com.example.crypto.analiziton.repository.CurrencyRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class CurrencyManipulationInDB {

    private final CurrencyRepository currencyRepository;

    public void saveCurrencyEntityInDB(CurrencyEntity currencyEntity){
      currencyRepository.saveAndFlush(currencyEntity);
    }
}
