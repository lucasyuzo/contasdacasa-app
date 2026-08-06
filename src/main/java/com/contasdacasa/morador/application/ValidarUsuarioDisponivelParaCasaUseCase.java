package com.contasdacasa.morador.application;

import com.contasdacasa.casa.domain.Casa;
import com.contasdacasa.morador.domain.MoradorRepository;
import com.contasdacasa.morador.domain.UsuarioJaEhMoradorDaCasaException;
import com.contasdacasa.usuario.domain.Usuario;
import org.springframework.stereotype.Service;

@Service
public class ValidarUsuarioDisponivelParaCasaUseCase {

  private final MoradorRepository moradorRepository;

  public ValidarUsuarioDisponivelParaCasaUseCase(MoradorRepository moradorRepository) {
    this.moradorRepository = moradorRepository;
  }

  public void executar(Usuario usuario, Casa casa) {
    if (moradorRepository.existePorUsuarioECasa(usuario.getId(), casa.getId())) {
      throw new UsuarioJaEhMoradorDaCasaException(usuario.getId(), casa.getId());
    }
  }
}
