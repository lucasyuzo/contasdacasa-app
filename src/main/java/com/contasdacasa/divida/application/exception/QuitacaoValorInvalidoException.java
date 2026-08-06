package com.contasdacasa.divida.application.exception;

import com.contasdacasa.shared.domain.DomainException;

public class QuitacaoValorInvalidoException extends DomainException {

    public QuitacaoValorInvalidoException() {
        super("Valor da quitacao deve ser maior que zero");
    }
}
