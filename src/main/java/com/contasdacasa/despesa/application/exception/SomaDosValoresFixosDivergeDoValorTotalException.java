package com.contasdacasa.despesa.application.exception;

import com.contasdacasa.shared.domain.DomainException;

import java.math.BigDecimal;

public class SomaDosValoresFixosDivergeDoValorTotalException extends DomainException {

    public SomaDosValoresFixosDivergeDoValorTotalException(BigDecimal valorTotal, BigDecimal soma) {
        super(
                "Soma dos valores fixos ("
                        + soma
                        + ") diverge do valor total da despesa ("
                        + valorTotal
                        + ")");
    }
}
