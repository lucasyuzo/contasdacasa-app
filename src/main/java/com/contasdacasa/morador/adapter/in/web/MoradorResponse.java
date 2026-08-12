package com.contasdacasa.morador.adapter.in.web;

import lombok.Getter;

import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
class MoradorResponse extends RepresentationModel<MoradorResponse> {

    private final UUID id;
    private final String nome;
    private final UUID casaId;
    private final UUID usuarioId;
    private final BigDecimal renda;

    MoradorResponse(UUID id, String nome, UUID casaId, UUID usuarioId, BigDecimal renda) {
        this.id = id;
        this.nome = nome;
        this.casaId = casaId;
        this.usuarioId = usuarioId;
        this.renda = renda;
    }
}
