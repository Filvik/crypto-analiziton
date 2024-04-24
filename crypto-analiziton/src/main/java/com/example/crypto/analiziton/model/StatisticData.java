package com.example.crypto.analiziton.model;

import lombok.Data;

@Data
public class StatisticData {
    private String currencyName;
    private long ascending;
    private long descending;
    private long neutral;
}
