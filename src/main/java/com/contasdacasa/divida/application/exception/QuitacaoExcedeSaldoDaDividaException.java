package com.contasdacasa.divida.application.exception;

import com.contasdacasa.shared.domain.DomainException;

import java.util.UUID;

public class QuitacaoExcedeSaldoDaDividaException extends DomainException {

    public QuitacaoExcedeSaldoDaDividaException(UUID dividaId) {
        super("Quitacao excede o saldo da divida " + dividaId);
    }
}
