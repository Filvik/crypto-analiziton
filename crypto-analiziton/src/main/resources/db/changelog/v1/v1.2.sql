CREATE TABLE volume (
    id SERIAL PRIMARY KEY,
    currency_name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    amount_of_base_currency DOUBLE PRECISION NOT NULL,
    transaction_amount DOUBLE PRECISION,
    time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    is_buyer VARCHAR(255),
    CONSTRAINT unique_constraint UNIQUE (currency_name, price, amount_of_base_currency, time, is_buyer)
);

