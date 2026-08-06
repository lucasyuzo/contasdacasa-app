package com.contasdacasa.despesa.adapter.in.web;

import com.contasdacasa.despesa.application.domain.Natureza;
import com.contasdacasa.despesa.application.domain.TipoRateio;
import com.contasdacasa.divida.adapter.in.web.DividaResponse;

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
    private final TipoRateio tipoRateio;
    private final UUID pagadorId;
    private final List<UUID> participantesIds;
    private final LocalDate dataVencimento;
    private final List<DividaResponse> dividas;

    DespesaResponse(
            UUID id,
            UUID casaId,
            BigDecimal valor,
            Natureza natureza,
            TipoRateio tipoRateio,
            UUID pagadorId,
            List<UUID> participantesIds,
            LocalDate dataVencimento,
            List<DividaResponse> dividas) {
        this.id = id;
        this.casaId = casaId;
        this.valor = valor;
        this.natureza = natureza;
        this.tipoRateio = tipoRateio;
        this.pagadorId = pagadorId;
        this.participantesIds = participantesIds;
        this.dataVencimento = dataVencimento;
        this.dividas = dividas;
    }
}
