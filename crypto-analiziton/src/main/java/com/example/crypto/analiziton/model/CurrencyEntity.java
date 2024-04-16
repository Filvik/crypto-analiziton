package com.example.crypto.analiziton.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

@Entity
@Table(name = "currency")
@Data
public class CurrencyEntity {

    @Schema(description = "Идентификатор")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Имя валюты")
    @Column(name = "currency_name", nullable = false)
    private String currencyName;

    @Schema(description = "Направления изменения относительно предыдущей цены")
    @Column(name = "tick_direction_for_last_price")
    private String tickDirection;

    @Schema(description = "Текущая цена")
    @Column(name = "price", nullable = false)
    private double price;

    @Schema(description = "Цена лучшего предложения покупки")
    @Column(name = "bid_price")
    private double bidPrice;

    @Schema(description = "Размер лучшего предложения покупки")
    @Column(name = "bid_size")
    private double bidSize;

    @Schema(description = "Цена лучшего предложения продажи")
    @Column(name = "ask_price")
    private double askPrice;

    @Schema(description = "Размер лучшего предложения продажи")
    @Column(name = "ask_size")
    private double askSize;

    @Schema(description = "Время получения")
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;
}
