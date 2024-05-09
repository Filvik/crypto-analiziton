CREATE MATERIALIZED VIEW view_currency_volumes AS
WITH calculated_ticks AS (
    SELECT
        currency_name,
        COALESCE(LAG(created_at) OVER (PARTITION BY currency_name ORDER BY created_at), TIMESTAMP '-infinity') AS tick_start,
        created_at AS tick_end
    FROM
        currency
),
extended_ticks AS (
    SELECT
        currency_name,
        tick_start,
        tick_end,
        COALESCE(LAG(tick_end) OVER (PARTITION BY currency_name ORDER BY tick_end), TIMESTAMP '-infinity') AS prev_tick_end
    FROM
        calculated_ticks
)
SELECT
    et.currency_name,
    et.tick_start,
    et.tick_end,
    COALESCE(SUM(v.amount_of_base_currency), 0) AS total_volume
FROM
    extended_ticks et
LEFT JOIN
    volume v ON v.currency_name = et.currency_name
              AND v.time >= et.prev_tick_end
              AND v.time < et.tick_end
GROUP BY
    et.currency_name, et.tick_start, et.tick_end;

CREATE INDEX idx_currency_name_created_at ON currency(currency_name, created_at);
CREATE INDEX idx_volume_currency_name_time ON volume(currency_name, time);
CREATE INDEX idx_view_currency_volume_name ON view_currency_volumes(currency_name);
CREATE INDEX idx_view_currency_volume_tick_start ON view_currency_volumes(tick_start);
CREATE INDEX idx_view_currency_volume_tick_end ON view_currency_volumes(tick_end);
