package com.example.crypto.analiziton.controller;

import com.example.crypto.analiziton.model.StatisticData;
import com.example.crypto.analiziton.service.ReceiveDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class ReceiveDataController {

    private final ReceiveDataService receiveDataService;

    @GetMapping(value = "/getData")
    @Operation(summary = "Получение данных", tags = "Контроллер получение данных")
    public StatisticData getData(@RequestParam @Parameter(description = "Валютная пара") String currencyName,
                                 @RequestParam @Parameter(description = "Дата начала периода") Long dateStart,
                                 @RequestParam @Parameter(description = "Дата окончание периода") Long dateStop) {
        log.info("Вызван метод getData для  = " + currencyName + " с " + dateStart + " до " + dateStop);
        return receiveDataService.receiveDateFromDB(currencyName, dateStart, dateStop);
    }
}
