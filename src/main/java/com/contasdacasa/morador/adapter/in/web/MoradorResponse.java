package com.contasdacasa.morador.adapter.in.web;

import java.util.UUID;
import org.springframework.hateoas.RepresentationModel;

class MoradorResponse extends RepresentationModel<MoradorResponse> {

  private final UUID id;
  private final String nome;
  private final UUID casaId;

  MoradorResponse(UUID id, String nome, UUID casaId) {
    this.id = id;
    this.nome = nome;
    this.casaId = casaId;
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
}
