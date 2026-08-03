package com.contasdacasa.morador.application;

import com.contasdacasa.casa.domain.Casa;
import com.contasdacasa.morador.domain.Morador;
import com.contasdacasa.morador.domain.MoradorRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarMoradorUseCase {

  private final MoradorRepository moradorRepository;

  public CriarMoradorUseCase(MoradorRepository moradorRepository) {
    this.moradorRepository = moradorRepository;
  }

  public Morador executar(Casa casa, String nome) {
    return moradorRepository.salvar(Morador.adicionar(casa.getId(), nome));
  }
}
