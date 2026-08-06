package com.contasdacasa.casa.application.usecase;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.casa.application.port.CasaPort;
import org.springframework.stereotype.Service;

@Service
public class CriarCasaUseCase {

  private final CasaPort casaPort;

  public CriarCasaUseCase(CasaPort casaPort) {
    this.casaPort = casaPort;
  }

  public Casa executar(String nome) {
    return casaPort.salvar(Casa.criar(nome));
  }
}
