package com.contasdacasa.usuario.application;

import com.contasdacasa.usuario.domain.Usuario;
import com.contasdacasa.usuario.domain.UsuarioNaoEncontradoException;
import com.contasdacasa.usuario.domain.UsuarioRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BuscarUsuarioUseCase {

  private final UsuarioRepository usuarioRepository;

  public BuscarUsuarioUseCase(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public Usuario executar(UUID id) {
    return usuarioRepository
        .buscarPorId(id)
        .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
  }
}
