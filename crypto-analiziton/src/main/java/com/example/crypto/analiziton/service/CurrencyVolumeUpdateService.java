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
                WITH next_tick AS (
                      SELECT c.currency_name, c.created_at AS current_tick, COALESCE(MIN(nc.created_at), NOW()) AS next_tick
                      FROM currency c
                      LEFT JOIN currency nc ON nc.currency_name = c.currency_name AND nc.created_at > c.created_at
                      GROUP BY c.currency_name, c.created_at
                  )
                  UPDATE currency c
                  SET volume = (
                      SELECT SUM(v.amount_of_base_currency)
                      FROM volume v
                      JOIN next_tick nt ON nt.currency_name = v.currency_name
                      WHERE v.currency_name = c.currency_name
                        AND v.time >= c.created_at
                        AND v.time < nt.next_tick
                  )
                  WHERE EXISTS (
                      SELECT 1
                      FROM volume v
                      JOIN next_tick nt ON nt.currency_name = v.currency_name
                      WHERE v.currency_name = c.currency_name
                        AND v.time >= c.created_at
                        AND v.time < nt.next_tick
                  );
                """;
        return jdbcTemplate.update(sql);
    }
}

