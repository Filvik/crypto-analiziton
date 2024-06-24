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
                REFRESH MATERIALIZED VIEW view_currency_volumes;
                UPDATE currency AS c
                SET volume = vcv.total_volume
                FROM view_currency_volumes AS vcv
                WHERE c.created_at = vcv.tick_end;
                """;
        return jdbcTemplate.update(sql);
    }
}

