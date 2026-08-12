package com.contasdacasa.divida.adapter.in.web;

import lombok.Getter;

import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
public class DividaResponse extends RepresentationModel<DividaResponse> {

    private final UUID id;
    private final UUID despesaId;
    private final UUID participanteId;
    private final UUID pagadorId;
    private final BigDecimal valor;
    private final BigDecimal saldo;
    private final boolean pendente;
    private final List<QuitacaoResponse> quitacoes;

    DividaResponse(
            UUID id,
            UUID despesaId,
            UUID participanteId,
            UUID pagadorId,
            BigDecimal valor,
            BigDecimal saldo,
            boolean pendente,
            List<QuitacaoResponse> quitacoes) {
        this.id = id;
        this.despesaId = despesaId;
        this.participanteId = participanteId;
        this.pagadorId = pagadorId;
        this.valor = valor;
        this.saldo = saldo;
        this.pendente = pendente;
        this.quitacoes = quitacoes;
    }
}
