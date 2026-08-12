package com.contasdacasa.despesa.application.exception;

import com.contasdacasa.shared.domain.DomainException;

import java.util.UUID;

public class ParticipanteSemValorFixoException extends DomainException {

    public ParticipanteSemValorFixoException(UUID moradorId) {
        super("Morador " + moradorId + " nao possui valor fixo definido para rateio por valor fixo");
    }
}
