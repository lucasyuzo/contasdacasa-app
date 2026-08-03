package com.contasdacasa.morador.application;

import com.contasdacasa.morador.domain.Morador;
import com.contasdacasa.morador.domain.MoradorNaoEncontradoException;
import com.contasdacasa.morador.domain.MoradorRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BuscarMoradorUseCase {

  private final MoradorRepository moradorRepository;

  public BuscarMoradorUseCase(MoradorRepository moradorRepository) {
    this.moradorRepository = moradorRepository;
  }

  public Morador executar(UUID id) {
    return moradorRepository
        .buscarPorId(id)
        .orElseThrow(() -> new MoradorNaoEncontradoException(id));
  }
}
