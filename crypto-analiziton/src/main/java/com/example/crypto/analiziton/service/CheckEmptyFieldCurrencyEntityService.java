package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CheckEmptyFieldCurrencyEntityService {

    public boolean check(CurrencyEntity currencyEntity) {
        if (currencyEntity == null) {
            return false;
        }
        boolean isCurrencyNameValid = currencyEntity.getCurrencyName() != null && !currencyEntity.getCurrencyName().trim().isEmpty();
        boolean isTickDirectionValid = currencyEntity.getTickDirection() != null && !currencyEntity.getTickDirection().trim().isEmpty();
        boolean isPriceValid = currencyEntity.getPrice() != 0;
        boolean isBidPriceValid = currencyEntity.getBidPrice() != 0;
        boolean isBidSizeValid = currencyEntity.getBidSize() != 0;
        boolean isAskPriceValid = currencyEntity.getAskPrice() != 0;
        boolean isAskSizeValid = currencyEntity.getAskSize() != 0;
        boolean isCreatedAtValid = currencyEntity.getCreatedAt() != null;

        return isCurrencyNameValid && isTickDirectionValid && isPriceValid && isBidPriceValid &&
                isBidSizeValid && isAskPriceValid && isAskSizeValid && isCreatedAtValid;
    }
}
