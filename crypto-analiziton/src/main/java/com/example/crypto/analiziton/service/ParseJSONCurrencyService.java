package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.converter.ConverterForCurrency;
import com.example.crypto.analiziton.exeption.ParseJSONCurrencyException;
import com.example.crypto.analiziton.model.CurrencyEntity;
import com.example.crypto.analiziton.model.CurrencyJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ParseJSONCurrencyService {

    private final ObjectMapper objectMapper;
    private final ConverterForCurrency converter;
    private final CurrencyManipulationInDB currencyManipulationInDB;

    public CurrencyEntity parseJson(TextMessage message) {
        try {
            CurrencyJson currencyJson = objectMapper.readValue(message.getPayload(), CurrencyJson.class);
            return converter.convertToEntity(currencyJson);
        } catch (IOException e) {
            log.error("Error parsing JSON message", e);
            throw new ParseJSONCurrencyException("Error parsing JSON message");
        }
    }
}