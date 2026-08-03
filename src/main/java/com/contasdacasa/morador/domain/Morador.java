package com.contasdacasa.morador.domain;

import java.util.UUID;

public class Morador {

  private final UUID id;
  private final String nome;
  private final UUID casaId;

  private Morador(UUID id, String nome, UUID casaId) {
    this.id = id;
    this.nome = nome;
    this.casaId = casaId;
  }

  public static Morador adicionar(UUID casaId, String nome) {
    return new Morador(UUID.randomUUID(), nome, casaId);
  }

  public static Morador reconstituir(UUID id, String nome, UUID casaId) {
    return new Morador(id, nome, casaId);
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
