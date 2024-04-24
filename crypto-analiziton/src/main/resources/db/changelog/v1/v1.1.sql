ALTER TABLE currency
ADD CONSTRAINT unique_currency_name_created_at UNIQUE (currency_name, created_at);
