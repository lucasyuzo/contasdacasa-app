package com.contasdacasa.morador.application.exception;

import com.contasdacasa.shared.domain.DomainException;

import java.util.UUID;

public class UsuarioJaEhMoradorDaCasaException extends DomainException {

    public UsuarioJaEhMoradorDaCasaException(UUID usuarioId, UUID casaId) {
        super("Usuario " + usuarioId + " ja eh morador da casa " + casaId);
    }
}
