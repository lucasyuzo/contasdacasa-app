package com.contasdacasa.morador.domain;

import java.util.UUID;

public class Morador {

  private final UUID id;
  private final String nome;
  private final UUID casaId;
  private final UUID usuarioId;

  private Morador(UUID id, String nome, UUID casaId, UUID usuarioId) {
    this.id = id;
    this.nome = nome;
    this.casaId = casaId;
    this.usuarioId = usuarioId;
  }

  public static Morador adicionar(UUID casaId, UUID usuarioId, String nome) {
    return new Morador(UUID.randomUUID(), nome, casaId, usuarioId);
  }

  public static Morador reconstituir(UUID id, String nome, UUID casaId, UUID usuarioId) {
    return new Morador(id, nome, casaId, usuarioId);
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
