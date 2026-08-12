package com.contasdacasa.morador.application.usecase;

import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.port.MoradorPort;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AtualizarRendaUseCase {

    private final MoradorPort moradorPort;

    public AtualizarRendaUseCase(MoradorPort moradorPort) {
        this.moradorPort = moradorPort;
    }

    public Morador executar(Morador morador, BigDecimal renda) {
        return moradorPort.salvar(morador.atualizarRenda(renda));
    }
}
