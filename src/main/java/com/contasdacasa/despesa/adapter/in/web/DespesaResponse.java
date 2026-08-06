package com.contasdacasa.despesa.adapter.in.web;

import com.contasdacasa.despesa.application.domain.Natureza;

import lombok.Getter;

import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
class DespesaResponse extends RepresentationModel<DespesaResponse> {

    private final UUID id;
    private final UUID casaId;
    private final BigDecimal valor;
    private final Natureza natureza;
    private final UUID pagadorId;
    private final List<UUID> participantesIds;
    private final LocalDate dataVencimento;
    private final List<DividaResponse> dividas;

    DespesaResponse(
            UUID id,
            UUID casaId,
            BigDecimal valor,
            Natureza natureza,
            UUID pagadorId,
            List<UUID> participantesIds,
            LocalDate dataVencimento,
            List<DividaResponse> dividas) {
        this.id = id;
        this.casaId = casaId;
        this.valor = valor;
        this.natureza = natureza;
        this.pagadorId = pagadorId;
        this.participantesIds = participantesIds;
        this.dataVencimento = dataVencimento;
        this.dividas = dividas;
    }
}
