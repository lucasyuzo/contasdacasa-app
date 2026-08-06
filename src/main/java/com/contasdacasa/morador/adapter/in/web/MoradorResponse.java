package com.contasdacasa.morador.adapter.in.web;

import org.springframework.hateoas.RepresentationModel;

import java.util.UUID;

class MoradorResponse extends RepresentationModel<MoradorResponse> {

    private final UUID id;
    private final String nome;
    private final UUID casaId;
    private final UUID usuarioId;

    MoradorResponse(UUID id, String nome, UUID casaId, UUID usuarioId) {
        this.id = id;
        this.nome = nome;
        this.casaId = casaId;
        this.usuarioId = usuarioId;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public UUID getCasaId() {
        return casaId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }
}
