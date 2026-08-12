package com.contasdacasa.usuario.application.usecase;

import com.contasdacasa.usuario.application.domain.Usuario;
import com.contasdacasa.usuario.application.exception.UsuarioNaoEncontradoException;
import com.contasdacasa.usuario.application.port.UsuarioPort;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarUsuarioUseCase {

    private final UsuarioPort usuarioPort;

    public BuscarUsuarioUseCase(UsuarioPort usuarioPort) {
        this.usuarioPort = usuarioPort;
    }

    public Usuario executar(UUID id) {
        return usuarioPort.buscarPorId(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }
}
