package com.example.crypto.analiziton.controller;

import com.example.crypto.analiziton.service.CurrencyVolumeUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class EnrichmentByVolumeCurrencyTable {

    private final CurrencyVolumeUpdateService updateService;

    @GetMapping("/update-volumes")
    public ResponseEntity<String> updateVolumes() {
        int updatedCount = updateService.updateCurrencyVolumes();
        return ResponseEntity.ok("Updated volumes for " + updatedCount + " currencies");
    }
}
