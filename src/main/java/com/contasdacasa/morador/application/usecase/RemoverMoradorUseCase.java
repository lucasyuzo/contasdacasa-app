package com.contasdacasa.morador.application.usecase;

import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.port.MoradorPort;
import org.springframework.stereotype.Service;

@Service
public class RemoverMoradorUseCase {

  private final MoradorPort moradorPort;

  public RemoverMoradorUseCase(MoradorPort moradorPort) {
    this.moradorPort = moradorPort;
  }

  public void executar(Morador morador) {
    moradorPort.remover(morador.getId());
  }
}
