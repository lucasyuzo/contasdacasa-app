package com.contasdacasa.divida.adapter.in.web;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
class QuitacaoResponse {

    private final UUID id;
    private final UUID dividaId;
    private final LocalDate data;
    private final BigDecimal valor;

    QuitacaoResponse(UUID id, UUID dividaId, LocalDate data, BigDecimal valor) {
        this.id = id;
        this.dividaId = dividaId;
        this.data = data;
        this.valor = valor;
    }
}
