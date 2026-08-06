package com.contasdacasa.despesa.application.exception;

import com.contasdacasa.shared.domain.DomainException;

import java.util.UUID;

public class ParticipanteSemRendaException extends DomainException {

    public ParticipanteSemRendaException(UUID moradorId) {
        super("Morador " + moradorId + " nao possui renda cadastrada para rateio por renda");
    }
}
