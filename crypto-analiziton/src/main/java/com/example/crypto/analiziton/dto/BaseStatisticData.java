package com.example.crypto.analiziton.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class BaseStatisticData {
    private String currencyName;
    private long ascendingTicks;
    private long descendingTicks;
    private long neutralTicks;
    private long allCountingTicks;
    private Timestamp startTime;
    private Timestamp stopTime;
}
