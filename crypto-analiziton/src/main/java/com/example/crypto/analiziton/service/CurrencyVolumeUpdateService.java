package com.example.crypto.analiziton.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyVolumeUpdateService {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public int updateCurrencyVolumes() {
        String sql = """
                UPDATE currency AS c
                SET volume = vcv.total_volume
                FROM view_currency_volume AS vcv
                WHERE c.currency_name = vcv.currency_name
                AND (c.created_at = vcv.tick_end OR (vcv.tick_end = TIMESTAMP 'infinity' AND c.created_at > vcv.tick_start));
                """;
        return jdbcTemplate.update(sql);
    }
}

