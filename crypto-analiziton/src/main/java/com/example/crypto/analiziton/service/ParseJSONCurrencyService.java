package com.example.crypto.analiziton.service;

import com.example.crypto.analiziton.converter.ConverterForCurrency;
import com.example.crypto.analiziton.exeption.ParseJSONCurrencyException;
import com.example.crypto.analiziton.model.CurrencyEntity;
import com.example.crypto.analiziton.model.CurrencyJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class ParseJSONCurrencyService {

    private final ObjectMapper objectMapper;
    private final ConverterForCurrency converter;

    public CurrencyEntity parseJson(TextMessage message) {
        try {
            CurrencyJson currencyJson = objectMapper.readValue(message.getPayload(), CurrencyJson.class);
            return converter.convertToEntity(currencyJson);
        } catch (IOException e) {
            log.warn("Error parsing JSON message", e);
            throw new ParseJSONCurrencyException("Error parsing JSON message");
        }
    }
}