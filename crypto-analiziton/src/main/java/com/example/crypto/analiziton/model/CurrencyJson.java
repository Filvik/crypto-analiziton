package com.example.crypto.analiziton.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyJson {
    private String topic;
    private String type;
    @JsonProperty("data")
    private DataCurrency dataCurrency;
    private long cs;
    private long ts;

    @JsonIgnoreProperties
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataCurrency {
        private String symbol;
        private String tickDirection;
        private double price24hPcnt;
        private double lastPrice;
        private double turnover24h;
        private double volume24h;
        @JsonProperty("bid1Price")
        private double bidPrice;
        @JsonProperty("bid1Size")
        private double bidSize;
        @JsonProperty("ask1Price")
        private double askPrice;
        @JsonProperty("ask1Size")
        private double askSize;
    }
}
