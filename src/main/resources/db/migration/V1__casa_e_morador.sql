CREATE TABLE casa
(
    id   UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL
);

CREATE TABLE morador
(
    id      UUID PRIMARY KEY,
    nome    VARCHAR(255) NOT NULL,
    casa_id UUID         NOT NULL REFERENCES casa (id)
);
