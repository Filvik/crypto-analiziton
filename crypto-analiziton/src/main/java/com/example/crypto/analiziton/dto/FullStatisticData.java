package com.example.crypto.analiziton.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FullStatisticData {
    private String currencyName;
    private String tickDirection;
    private double price;
    private Timestamp createdAt;

}
