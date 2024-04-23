package com.example.crypto.analiziton.component;

import com.example.crypto.analiziton.model.CurrencyEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CheckEmptyFieldCurrencyEntityComponent {

    public boolean checkBaseFields(CurrencyEntity currencyEntity) {
        if (currencyEntity == null) {
            return false;
        }
        boolean isCurrencyNameValid = currencyEntity.getCurrencyName() != null && !currencyEntity.getCurrencyName().trim().isEmpty();
        boolean isTickDirectionValid = currencyEntity.getTickDirection() != null && !currencyEntity.getTickDirection().trim().isEmpty();
        boolean isPriceValid = currencyEntity.getPrice() != 0;
        boolean isCreatedAtValid = currencyEntity.getCreatedAt() != null;

        return isCurrencyNameValid && isTickDirectionValid && isPriceValid && isCreatedAtValid;
    }

    public boolean checkFullFieldsBeforeRecordInBD(CurrencyEntity currencyEntity) {
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
