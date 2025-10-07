CREATE TABLE empresa (
                         id BIGSERIAL PRIMARY KEY,
                         cnpj VARCHAR(255) NOT NULL UNIQUE,
                         nome VARCHAR(255) NOT NULL
);

