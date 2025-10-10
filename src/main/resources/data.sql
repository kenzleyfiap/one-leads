
INSERT INTO contato (nome, email, telefone, observacoes)
VALUES ('Teste', 'teste@mail.com', '', '');

INSERT INTO contato (nome, email, telefone, observacoes)
VALUES ('Teste2', 'teste2@mail.com', '', '');

INSERT INTO empresa (cnpj, nome)
VALUES ('35444131000142', 'Teste empresa');

INSERT INTO empresa (id,cnpj, nome)
VALUES (50,'84901716000150', 'Teste empresa');

INSERT INTO empresa_contato
(empresa_id, contato_id)
VALUES(1, 1);

INSERT INTO empresa_contato
(empresa_id, contato_id)
VALUES(50, 1);

INSERT INTO empresa_contato
(empresa_id, contato_id)
VALUES(50, 2);
