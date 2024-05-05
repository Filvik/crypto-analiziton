CREATE INDEX idx_currency_name ON currency(currency_name);
CREATE INDEX idx_currency_created_at ON currency(created_at);

CREATE INDEX idx_volume_currency_name ON volume(currency_name);
CREATE INDEX idx_volume_time ON volume(time);
