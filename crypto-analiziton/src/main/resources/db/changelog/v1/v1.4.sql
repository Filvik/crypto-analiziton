CREATE OR REPLACE VIEW view_currency_volume AS
WITH calculated_ticks AS (
    SELECT
        currency_name,
        COALESCE(LAG(created_at) OVER (PARTITION BY currency_name ORDER BY created_at), TIMESTAMP '-infinity') AS tick_start,
        created_at AS tick_end
    FROM
        currency
)
SELECT
    ct.currency_name,
    ct.tick_start,
    ct.tick_end,
    COALESCE(SUM(v.amount_of_base_currency), 0) AS total_volume
FROM
    calculated_ticks ct
LEFT JOIN
    volume v ON v.currency_name = ct.currency_name
              AND v.time >= ct.tick_start
              AND v.time < ct.tick_end
GROUP BY
    ct.currency_name, ct.tick_start, ct.tick_end;
