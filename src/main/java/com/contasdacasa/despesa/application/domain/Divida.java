package com.contasdacasa.despesa.application.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Divida {

    private final UUID id;
    private final UUID despesaId;
    private final UUID participanteId;
    private final UUID pagadorId;
    private final BigDecimal valor;

    private Divida(UUID id, UUID despesaId, UUID participanteId, UUID pagadorId, BigDecimal valor) {
        this.id = id;
        this.despesaId = despesaId;
        this.participanteId = participanteId;
        this.pagadorId = pagadorId;
        this.valor = valor;
    }

    static Divida gerar(UUID despesaId, UUID participanteId, UUID pagadorId, BigDecimal valor) {
        return new Divida(UUID.randomUUID(), despesaId, participanteId, pagadorId, valor);
    }

    public static Divida reconstituir(
            UUID id, UUID despesaId, UUID participanteId, UUID pagadorId, BigDecimal valor) {
        return new Divida(id, despesaId, participanteId, pagadorId, valor);
    }
}
