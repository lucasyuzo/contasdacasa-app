CREATE TABLE quitacao
(
    id        UUID PRIMARY KEY,
    divida_id UUID           NOT NULL REFERENCES divida (id),
    data      DATE           NOT NULL,
    valor     NUMERIC(12, 2) NOT NULL
);
