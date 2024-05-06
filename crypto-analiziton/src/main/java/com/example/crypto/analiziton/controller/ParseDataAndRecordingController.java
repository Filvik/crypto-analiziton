package com.example.crypto.analiziton.controller;

import com.example.crypto.analiziton.service.RecordFileInDBService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
@Slf4j
public class ParseDataAndRecordingController {

    private final RecordFileInDBService recordFileInDBService;

    @GetMapping(value = "/recording")
    @Operation(summary = "Запись данных из csv файла в БД", tags = "Контроллер записи данных из файла")
    public ResponseEntity<?> recordingInDB() {
        log.info("Called method recordingInDB.");
        try {
            recordFileInDBService.saveInDBFromFolder();
        }
       catch (Exception e){
           log.warn("Error save in BD.");
           return ResponseEntity.ok("Error save in BD.");
       }
        return ResponseEntity.ok("The data was successfully saved to BD.");
    }
}
