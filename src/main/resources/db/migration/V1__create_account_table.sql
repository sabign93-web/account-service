CREATE TABLE account
(
    id BIGSERIAL PRIMARY KEY,

    iban VARCHAR(34) NOT NULL UNIQUE,

    owner_name VARCHAR(255) NOT NULL,

    balance NUMERIC(19,2) NOT NULL,

    currency VARCHAR(3) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);