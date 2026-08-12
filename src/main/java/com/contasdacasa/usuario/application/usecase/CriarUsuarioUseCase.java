package com.contasdacasa.usuario.application.usecase;

import com.contasdacasa.usuario.application.domain.Usuario;
import com.contasdacasa.usuario.application.port.UsuarioPort;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CriarUsuarioUseCase {

    private final UsuarioPort usuarioPort;

    public CriarUsuarioUseCase(UsuarioPort usuarioPort) {
        this.usuarioPort = usuarioPort;
    }

    public Usuario executar(String nome) {
        UUID id = UUID.randomUUID();
        Usuario usuario = new Usuario(id, nome);
        return usuarioPort.salvar(usuario);
    }
}
