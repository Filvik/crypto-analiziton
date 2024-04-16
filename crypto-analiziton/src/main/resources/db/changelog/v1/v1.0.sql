CREATE TABLE currency (
    id SERIAL PRIMARY KEY,
    currency_name VARCHAR(255) NOT NULL,
    tick_direction_for_last_price VARCHAR(255) DEFAULT 'Invariably',
    price NUMERIC NOT NULL,
    bid_price NUMERIC,
    bid_size NUMERIC,
    ask_price NUMERIC,
    ask_size NUMERIC,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
