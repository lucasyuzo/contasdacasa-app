package com.contasdacasa.morador.application;

import com.contasdacasa.casa.domain.Casa;
import com.contasdacasa.morador.domain.Morador;
import com.contasdacasa.morador.domain.MoradorRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ListarMoradoresUseCase {

  private final MoradorRepository moradorRepository;

  public ListarMoradoresUseCase(MoradorRepository moradorRepository) {
    this.moradorRepository = moradorRepository;
  }

  public List<Morador> executar(Casa casa) {
    return moradorRepository.listarPorCasa(casa.getId());
  }
}
