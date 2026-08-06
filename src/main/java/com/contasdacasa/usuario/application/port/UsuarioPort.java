package com.contasdacasa.usuario.application.port;

import com.contasdacasa.usuario.application.domain.Usuario;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioPort {

  Usuario salvar(Usuario usuario);

  Optional<Usuario> buscarPorId(UUID id);
}
