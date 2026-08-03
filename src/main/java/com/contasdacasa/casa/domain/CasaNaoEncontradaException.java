package com.contasdacasa.casa.domain;

import com.contasdacasa.shared.domain.DomainException;
import java.util.UUID;

public class CasaNaoEncontradaException extends DomainException {

  public CasaNaoEncontradaException(UUID id) {
    super("Casa nao encontrada: " + id);
  }
}
