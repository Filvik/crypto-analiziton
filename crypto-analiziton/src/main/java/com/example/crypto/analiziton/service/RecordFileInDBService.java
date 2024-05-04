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
        int savedLines = 0;
        int skippedLines = 0;
        String currencyName = file.getName().substring(0, file.getName().indexOf("-"));
        String[] expectedHeader = {"id", "price", "qty", "quote_qty", "time", "is_buyer_maker"};

        try (CSVReader reader = new CSVReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String[] firstLine = reader.peek();
            if (isHeader(firstLine, expectedHeader)) {
                reader.readNext();
            }

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                try {
                    DataFromBinanceAboutVolume volume = parseVolumeDataFromFile.parseVolumeData(nextLine, currencyName);
                    volumeRepository.save(volume);
                    savedLines++;
                } catch (Exception e) {
                    skippedLines++;
                }
            }
            log.info("Recorded: " + savedLines + " lines from file: " + file.getName());
            log.warn("Skipped: " + skippedLines + " lines due to errors.");
        } catch (IOException | CsvException e) {
            log.warn("Error processing file: " + file.getName() + ", " + e.getMessage());
        }
    }

    private boolean isHeader(String[] firstLine, String[] expectedHeader) {
        if (firstLine == null || firstLine.length != expectedHeader.length) return false;
        for (int i = 0; i < firstLine.length; i++) {
            if (!firstLine[i].equalsIgnoreCase(expectedHeader[i])) {
                return false;
            }
        }
        return true;
    }

}
