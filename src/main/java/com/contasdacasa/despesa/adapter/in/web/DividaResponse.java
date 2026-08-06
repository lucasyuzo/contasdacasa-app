package com.contasdacasa.despesa.adapter.in.web;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
class DividaResponse {

    private final UUID id;
    private final UUID participanteId;
    private final UUID pagadorId;
    private final BigDecimal valor;

    DividaResponse(UUID id, UUID participanteId, UUID pagadorId, BigDecimal valor) {
        this.id = id;
        this.participanteId = participanteId;
        this.pagadorId = pagadorId;
        this.valor = valor;
    }
}
