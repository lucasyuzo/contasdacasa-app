package com.contasdacasa.casa.application;

import com.contasdacasa.casa.domain.Casa;
import com.contasdacasa.casa.domain.CasaNaoEncontradaException;
import com.contasdacasa.casa.domain.CasaRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BuscarCasaUseCase {

  private final CasaRepository casaRepository;

  public BuscarCasaUseCase(CasaRepository casaRepository) {
    this.casaRepository = casaRepository;
  }

  public Casa executar(UUID id) {
    return casaRepository.buscarPorId(id).orElseThrow(() -> new CasaNaoEncontradaException(id));
  }
}
