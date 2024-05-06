package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.dto.FullDetailsStatisticData;
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

    public void getCsvForListFullStatisticData(List<FullDetailsStatisticData> fullDetailsStatisticDataList) {
        if (!fullDetailsStatisticDataList.isEmpty()) {
            try (BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFilePath))) {
                // Write CSV header
                csvWriter.append("currencyName, price, volume, createdAt\n");
                // Write CSV data
                for (FullDetailsStatisticData data : fullDetailsStatisticDataList) {
                    csvWriter.append(String.join(",",
                            String.valueOf(data.getCurrencyName()),
                            String.valueOf(data.getPrice()),
                            String.valueOf(data.getVolume()),
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
