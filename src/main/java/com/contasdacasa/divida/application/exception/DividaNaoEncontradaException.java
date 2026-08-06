package com.contasdacasa.divida.application.exception;

import com.contasdacasa.shared.domain.DomainException;

import java.util.UUID;

public class DividaNaoEncontradaException extends DomainException {

    public DividaNaoEncontradaException(UUID id) {
        super("Divida nao encontrada: " + id);
    }
}
