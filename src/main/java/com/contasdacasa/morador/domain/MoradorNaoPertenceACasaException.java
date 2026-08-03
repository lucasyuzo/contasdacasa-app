package com.contasdacasa.morador.domain;

import com.contasdacasa.shared.domain.DomainException;
import java.util.UUID;

public class MoradorNaoPertenceACasaException extends DomainException {

  public MoradorNaoPertenceACasaException(UUID moradorId, UUID casaId) {
    super("Morador " + moradorId + " nao pertence a casa " + casaId);
  }
}
