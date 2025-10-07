CREATE TABLE empresa_contatos (
                                  empresa_id BIGINT NOT NULL,
                                  contatos_id BIGINT NOT NULL,
                                  PRIMARY KEY (empresa_id, contatos_id),
                                  FOREIGN KEY (empresa_id) REFERENCES empresa(id),
                                  FOREIGN KEY (contatos_id) REFERENCES contato(id)
);
