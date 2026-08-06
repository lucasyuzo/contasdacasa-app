package com.contasdacasa.usuario.application;

import com.contasdacasa.usuario.domain.Usuario;
import com.contasdacasa.usuario.domain.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class CriarUsuarioUseCase {

  private final UsuarioRepository usuarioRepository;

  public CriarUsuarioUseCase(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public Usuario executar(String nome) {
    return usuarioRepository.salvar(Usuario.criar(nome));
  }
}
