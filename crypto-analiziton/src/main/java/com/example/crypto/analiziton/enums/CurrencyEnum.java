package com.example.crypto.analiziton.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum for representing different currency pairs in the cryptocurrency trading context.
 */
@Getter
@RequiredArgsConstructor
public enum CurrencyEnum {
    BTCUSDT("BTCUSDT"),
    ETHUSDT("ETHUSDT"),
    XRPUSDT("XRPUSDT"),
    LTCUSDT("LTCUSDT"),
    ADAUSDT("ADAUSDT"),
    DOGEUSDT("DOGEUSDT"),
    DOTUSDT("DOTUSDT"),
    LINKUSDT("LINKUSDT"),
    SOLUSDT("SOLUSDT"),
    BCHUSDT("BCHUSDT"),
    UNIUSDT("UNIUSDT"),
    MATICUSDT("MATICUSDT"),
    EOSUSDT("EOSUSDT"),
    XLMUSDT("XLMUSDT"),
    TRXUSDT("TRXUSDT"),
    FILUSDT("FILUSDT"),
    AAVEUSDT("AAVEUSDT"),
    KSMUSDT("KSMUSDT"),
    ALGOUSDT("ALGOUSDT"),
    XTZUSDT("XTZUSDT");

    private final String symbol;
}
