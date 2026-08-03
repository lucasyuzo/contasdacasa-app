package com.contasdacasa.casa.application;

import com.contasdacasa.casa.domain.Casa;
import com.contasdacasa.casa.domain.CasaRepository;
import org.springframework.stereotype.Component;

@Component
public class CriarCasaUseCase {

  private final CasaRepository casaRepository;

  public CriarCasaUseCase(CasaRepository casaRepository) {
    this.casaRepository = casaRepository;
  }

  public Casa executar(String nome) {
    return casaRepository.salvar(Casa.criar(nome));
  }
}
