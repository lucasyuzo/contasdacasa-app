package com.contasdacasa.morador.domain;

import com.contasdacasa.shared.domain.DomainException;
import java.util.UUID;

public class MoradorNaoEncontradoException extends DomainException {

  public MoradorNaoEncontradoException(UUID id) {
    super("Morador nao encontrado: " + id);
  }
}
