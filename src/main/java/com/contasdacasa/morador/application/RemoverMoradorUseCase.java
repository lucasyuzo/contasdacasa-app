package com.contasdacasa.morador.application;

import com.contasdacasa.morador.domain.Morador;
import com.contasdacasa.morador.domain.MoradorRepository;
import org.springframework.stereotype.Service;

@Service
public class RemoverMoradorUseCase {

  private final MoradorRepository moradorRepository;

  public RemoverMoradorUseCase(MoradorRepository moradorRepository) {
    this.moradorRepository = moradorRepository;
  }

  public void executar(Morador morador) {
    moradorRepository.remover(morador.getId());
  }
}
