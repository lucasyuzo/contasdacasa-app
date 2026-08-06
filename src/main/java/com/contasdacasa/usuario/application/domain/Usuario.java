package com.contasdacasa.usuario.application.domain;

import java.util.UUID;

public class Usuario {

  private final UUID id;
  private final String nome;

  private Usuario(UUID id, String nome) {
    this.id = id;
    this.nome = nome;
  }

  public static Usuario criar(String nome) {
    return new Usuario(UUID.randomUUID(), nome);
  }

  public static Usuario reconstituir(UUID id, String nome) {
    return new Usuario(id, nome);
  }

  public UUID getId() {
    return id;
  }

  public String getNome() {
    return nome;
  }
}
