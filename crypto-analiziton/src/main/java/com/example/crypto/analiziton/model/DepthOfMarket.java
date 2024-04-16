package com.example.crypto.analiziton.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepthOfMarket {
    private double bidPrice;
    private long bidSize;
    private double askPrice;
    private long askSize;
}
