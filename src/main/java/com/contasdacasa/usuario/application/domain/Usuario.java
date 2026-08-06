package com.contasdacasa.usuario.application.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Usuario {

  private final UUID id;
  private final String nome;

  public Usuario(UUID id, String nome) {
    this.id = id;
    this.nome = nome;
  }
}
