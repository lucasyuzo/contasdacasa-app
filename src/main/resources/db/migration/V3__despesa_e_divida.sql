CREATE TABLE despesa
(
    id              UUID PRIMARY KEY,
    casa_id         UUID           NOT NULL REFERENCES casa (id),
    valor           NUMERIC(12, 2) NOT NULL,
    natureza        VARCHAR(20)    NOT NULL,
    pagador_id      UUID           NOT NULL REFERENCES morador (id),
    data_vencimento DATE           NOT NULL
);

CREATE TABLE despesa_participante
(
    despesa_id UUID NOT NULL REFERENCES despesa (id),
    morador_id UUID NOT NULL REFERENCES morador (id),
    ordem      INT  NOT NULL,
    PRIMARY KEY (despesa_id, ordem)
);

CREATE TABLE divida
(
    id              UUID PRIMARY KEY,
    despesa_id      UUID           NOT NULL REFERENCES despesa (id),
    participante_id UUID           NOT NULL REFERENCES morador (id),
    pagador_id      UUID           NOT NULL REFERENCES morador (id),
    valor           NUMERIC(12, 2) NOT NULL
);
