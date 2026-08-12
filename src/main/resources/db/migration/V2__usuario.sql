CREATE TABLE usuario
(
    id   UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

ALTER TABLE morador
    ADD COLUMN usuario_id UUID NOT NULL REFERENCES usuario (id),
    ADD CONSTRAINT uk_morador_usuario_casa UNIQUE (usuario_id, casa_id);
