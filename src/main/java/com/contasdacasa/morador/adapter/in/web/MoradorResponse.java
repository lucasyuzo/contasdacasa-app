package com.contasdacasa.morador.adapter.in.web;

import java.util.UUID;
import org.springframework.hateoas.RepresentationModel;

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
