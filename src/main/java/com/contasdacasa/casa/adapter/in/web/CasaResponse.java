package com.contasdacasa.casa.adapter.in.web;

import lombok.Getter;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

@Getter
public class CasaResponse extends RepresentationModel<CasaResponse> {

    private final UUID id;
    private final String nome;

    CasaResponse(UUID id, String nome) {
        this.id = id;
        this.nome = nome;
    }
}
