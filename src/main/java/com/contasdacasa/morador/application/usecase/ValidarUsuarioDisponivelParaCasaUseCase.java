package com.contasdacasa.morador.application.usecase;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.morador.application.exception.UsuarioJaEhMoradorDaCasaException;
import com.contasdacasa.morador.application.port.MoradorPort;
import com.contasdacasa.usuario.application.domain.Usuario;
import org.springframework.stereotype.Service;

@Service
public class ValidarUsuarioDisponivelParaCasaUseCase {

  private final MoradorPort moradorPort;

  public ValidarUsuarioDisponivelParaCasaUseCase(MoradorPort moradorPort) {
    this.moradorPort = moradorPort;
  }

  public void executar(Usuario usuario, Casa casa) {
    if (moradorPort.existePorUsuarioECasa(usuario.getId(), casa.getId())) {
      throw new UsuarioJaEhMoradorDaCasaException(usuario.getId(), casa.getId());
    }
  }
}
