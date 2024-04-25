package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.dto.FullStatisticData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CsvWriterService {

    private static final String CSV_FILE_PATH = "result/data.csv";

    public void getCsvForListFullStatisticData(List<FullStatisticData> fullStatisticDataList) {

    }
}
