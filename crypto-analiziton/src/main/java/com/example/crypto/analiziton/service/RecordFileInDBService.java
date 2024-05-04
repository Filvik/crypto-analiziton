package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.helper.ParseVolumeDataFromFile;
import com.example.crypto.analiziton.model.DataFromBinanceAboutVolume;
import com.example.crypto.analiziton.repository.VolumeRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordFileInDBService {

    private final VolumeRepository volumeRepository;
    private final ParseVolumeDataFromFile parseVolumeDataFromFile;

    public void saveInDBFromFolder() {
        File folder = new File("file-from-binance");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (listOfFiles != null) {
            log.info("Number of files in folder: " + listOfFiles.length);
            for (File file : listOfFiles) {
                saveDataFromFile(file);
            }
        } else {
            log.info("The folder is empty.");
        }
    }

    private void saveDataFromFile(File file) {
        int i = 0;
        String currencyName = file.getName().substring(0, file.getName().indexOf("-"));
        try (CSVReader reader = new CSVReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.skip(1);
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    DataFromBinanceAboutVolume volume = parseVolumeDataFromFile.parseVolumeData(nextLine, currencyName);
                    volumeRepository.save(volume);
                    i++;
                } catch (Exception e) {
                    log.warn("Error recording line: " + e.getMessage());
                }
            }
            log.info("Recorded: " + i + " lines from file: " + file.getName());
        } catch (IOException | CsvException e) {
            log.warn("Error processing file: " + file.getName() + ", " + e.getMessage());
        }
    }
}
