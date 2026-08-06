package com.contasdacasa.casa.application.exception;

import com.contasdacasa.shared.domain.DomainException;
import java.util.UUID;

public class CasaNaoEncontradaException extends DomainException {

  public CasaNaoEncontradaException(UUID id) {
    super("Casa nao encontrada: " + id);
  }
}
