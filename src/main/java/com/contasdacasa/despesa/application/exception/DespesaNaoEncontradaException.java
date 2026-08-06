package com.contasdacasa.despesa.application.exception;

import com.contasdacasa.shared.domain.DomainException;

import java.util.UUID;

public class DespesaNaoEncontradaException extends DomainException {

    public DespesaNaoEncontradaException(UUID id) {
        super("Despesa nao encontrada: " + id);
    }
}
