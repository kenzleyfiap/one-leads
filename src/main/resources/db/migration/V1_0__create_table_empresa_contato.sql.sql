CREATE TABLE contato (
                         id BIGSERIAL PRIMARY KEY,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) NOT NULL UNIQUE,
                         telefone VARCHAR(255),
                         observacoes TEXT
);

CREATE TABLE empresa (
                         id BIGSERIAL PRIMARY KEY,
                         cnpj VARCHAR(255) NOT NULL UNIQUE,
                         nome VARCHAR(255) NOT NULL
);

CREATE TABLE empresa_contato (
                                 empresa_id BIGINT NOT NULL,
                                 contato_id BIGINT NOT NULL,
                                 PRIMARY KEY (empresa_id, contato_id),
                                 FOREIGN KEY (empresa_id) REFERENCES empresa(id) ON DELETE CASCADE,
                                 FOREIGN KEY (contato_id) REFERENCES contato(id) ON DELETE CASCADE
);

CREATE INDEX idx_empresa_contato_empresa ON empresa_contato(empresa_id);
CREATE INDEX idx_empresa_contato_contato ON empresa_contato(contato_id);