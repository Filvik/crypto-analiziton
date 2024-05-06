package com.example.crypto.analiziton.dto;

import lombok.Data;

@Data
public class FullDetailsStatisticData {
    private String currencyName;
    private double price;
    private double volume;
    private Long createdAt;

}
