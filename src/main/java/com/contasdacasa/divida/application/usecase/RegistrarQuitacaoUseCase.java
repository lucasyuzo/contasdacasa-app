package com.contasdacasa.divida.application.usecase;

import com.contasdacasa.divida.application.domain.Divida;
import com.contasdacasa.divida.application.domain.Quitacao;
import com.contasdacasa.divida.application.port.DividaPort;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class RegistrarQuitacaoUseCase {

    private final DividaPort dividaPort;

    public RegistrarQuitacaoUseCase(DividaPort dividaPort) {
        this.dividaPort = dividaPort;
    }

    public Quitacao executar(Divida divida, LocalDate data, BigDecimal valor) {
        Quitacao quitacao = divida.registrarQuitacao(data, valor);
        return dividaPort.registrarQuitacao(quitacao);
    }
}
