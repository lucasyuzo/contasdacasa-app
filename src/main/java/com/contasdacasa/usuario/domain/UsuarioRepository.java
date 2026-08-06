package com.contasdacasa.usuario.domain;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {

  Usuario salvar(Usuario usuario);

  Optional<Usuario> buscarPorId(UUID id);
}
