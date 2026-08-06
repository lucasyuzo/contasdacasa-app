package com.contasdacasa.usuario.application.usecase;

import com.contasdacasa.usuario.application.domain.Usuario;
import com.contasdacasa.usuario.application.port.UsuarioPort;
import org.springframework.stereotype.Service;

@Service
public class CriarUsuarioUseCase {

  private final UsuarioPort usuarioPort;

  public CriarUsuarioUseCase(UsuarioPort usuarioPort) {
    this.usuarioPort = usuarioPort;
  }

  public Usuario executar(String nome) {
    return usuarioPort.salvar(Usuario.criar(nome));
  }
}
