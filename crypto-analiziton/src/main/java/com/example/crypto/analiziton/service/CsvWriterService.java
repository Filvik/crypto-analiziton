package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.dto.FullStatisticData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class CsvWriterService {

    @Value("${csv.output.path}")
    private String csvFilePath;

    public void getCsvForListFullStatisticData(List<FullStatisticData> fullStatisticDataList) {
        if (!fullStatisticDataList.isEmpty()) {
            try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFilePath))) {
                // Write CSV header
                csvWriter.append("currencyName,tickDirection,price,createdAt\n");
                // Write CSV data
                for (FullStatisticData data : fullStatisticDataList) {
                    csvWriter.append(String.join(",",
                            String.valueOf(data.getCurrencyName()),
                            String.valueOf(data.getTickDirection()),
                            String.valueOf(data.getPrice()),
                            String.valueOf(data.getCreatedAt())));
                    csvWriter.append("\n");
                }
                log.info("CSV file '{}' has been successfully created.", csvFilePath);
            } catch (IOException e) {
                log.error("Error occurred while writing CSV file: {}", e.getMessage());
            }
        }
    }
}
