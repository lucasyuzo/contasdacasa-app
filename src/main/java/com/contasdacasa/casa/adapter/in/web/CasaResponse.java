package com.contasdacasa.casa.adapter.in.web;

import com.contasdacasa.shared.domain.TipoRateio;

import lombok.Getter;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
public class CasaResponse extends RepresentationModel<CasaResponse> {

    private final UUID id;
    private final String nome;
    private final TipoRateio rateioPadrao;

    CasaResponse(UUID id, String nome, TipoRateio rateioPadrao) {
        this.id = id;
        this.nome = nome;
        this.rateioPadrao = rateioPadrao;
    }
}
