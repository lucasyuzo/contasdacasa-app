package com.contasdacasa.usuario.adapter.in.web;

import java.util.UUID;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class UsuarioResponse extends RepresentationModel<UsuarioResponse> {

  private final UUID id;
  private final String nome;

  UsuarioResponse(UUID id, String nome) {
    this.id = id;
    this.nome = nome;
  }
}
