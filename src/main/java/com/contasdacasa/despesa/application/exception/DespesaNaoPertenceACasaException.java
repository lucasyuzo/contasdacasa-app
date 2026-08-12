package com.contasdacasa.despesa.application.exception;

import com.contasdacasa.shared.domain.DomainException;

import java.util.UUID;

public class DespesaNaoPertenceACasaException extends DomainException {

    public DespesaNaoPertenceACasaException(UUID despesaId, UUID casaId) {
        super("Despesa " + despesaId + " nao pertence a casa " + casaId);
    }
}
