package com.example.crypto.analiziton.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "volume", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"currencyName", "price", "amountOfBaseCurrency", "time", "isBuyer"})
})
@Data
public class DataFromBinanceAboutVolume {

    @Schema(description = "Идентификатор")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Имя валюты")
    @Column(name = "currency_name", nullable = false)
    private String currencyName;

    @Schema(description = "Текущая цена")
    @Column(name = "price", nullable = false)
    private double price;

    @Schema(description = "Количество базовой валюты")
    @Column(name = "amount_of_base_currency", nullable = false)
    private double amountOfBaseCurrency;

    @Schema(description = "Сумма сделки в валюте")
    @Column(name = "transaction_amount")
    private double transactionAmount;

    @Schema(description = "Время сделки")
    @Column(name = "time", nullable = false)
    private Timestamp time;

    @Schema(description = "Цена лучшего предложения продажи")
    @Column(name = "isBuyer")
    private String isBuyer;

}
