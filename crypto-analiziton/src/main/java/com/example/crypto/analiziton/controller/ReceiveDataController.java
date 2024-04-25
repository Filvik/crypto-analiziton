package com.example.crypto.analiziton.controller;

import com.example.crypto.analiziton.dto.FullStatisticData;
import com.example.crypto.analiziton.dto.BaseStatisticData;
import com.example.crypto.analiziton.service.CsvWriterService;
import com.example.crypto.analiziton.service.ReceiveDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class ReceiveDataController {

    private final ReceiveDataService receiveDataService;
    private final CsvWriterService csvWriterService;

    @GetMapping(value = "/getBaseData")
    @Operation(summary = "Получение общих данных за определенный период времени", tags = "Контроллер получение общих данных")
    public BaseStatisticData getBaseData(@RequestParam @Parameter(description = "Валютная пара") String currencyName,
                                         @RequestParam @Parameter(description = "Дата начала периода") Long dateStart,
                                         @RequestParam @Parameter(description = "Дата окончание периода") Long dateStop) {
        log.info("Вызван метод getBaseData для  = " + currencyName + " за период с " + dateStart + " до " + dateStop);
        return receiveDataService.receiveBaseDataFromDB(currencyName, dateStart, dateStop);
    }

    @GetMapping(value = "/getCollectionData")
    @Operation(summary = "Получение конкретных данных за определенный период времени", tags = "Контроллер получение конкретных данных")
    public ResponseEntity<?> getCollectionData(@RequestParam @Parameter(description = "Валютная пара") String currencyName,
                                               @RequestParam @Parameter(description = "Дата начала периода") Long dateStart,
                                               @RequestParam @Parameter(description = "Дата окончание периода") Long dateStop) {
        log.info("Вызван метод getCollectionData для  = " + currencyName + " с " + dateStart + " до " + dateStop);
        List<FullStatisticData> fullStatisticDataList = receiveDataService.receiveCollectionDateFromDB(currencyName, dateStart, dateStop);
        csvWriterService.getCsvForListFullStatisticData(fullStatisticDataList);
        return ResponseEntity.ok("The data was successfully received and saved to a CSV file.");
    }
}
