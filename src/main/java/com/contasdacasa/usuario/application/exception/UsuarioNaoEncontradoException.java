package com.contasdacasa.usuario.application.exception;

import com.contasdacasa.shared.domain.DomainException;

import java.util.UUID;

public class UsuarioNaoEncontradoException extends DomainException {

    public UsuarioNaoEncontradoException(UUID id) {
        super("Usuario nao encontrado: " + id);
    }
}
