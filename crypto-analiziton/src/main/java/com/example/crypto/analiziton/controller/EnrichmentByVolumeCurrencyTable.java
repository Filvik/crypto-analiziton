package com.example.crypto.analiziton.controller;

import com.example.crypto.analiziton.service.CurrencyVolumeUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class EnrichmentByVolumeCurrencyTable {

    private final CurrencyVolumeUpdateService updateService;

    @GetMapping("/addVolumes")
    @Operation(summary = "Запись данных из таблицы volume в таблицу currency", tags = "Контроллер обогащение объемами")
    public ResponseEntity<String> updateVolumes() {
        log.info("Called method updateVolumes.");
        int updatedCount = updateService.updateCurrencyVolumes();
        return ResponseEntity.ok("Updated volumes for " + updatedCount + " currencies");
    }
}
