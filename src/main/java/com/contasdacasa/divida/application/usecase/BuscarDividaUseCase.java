package com.contasdacasa.divida.application.usecase;

import com.contasdacasa.divida.application.domain.Divida;
import com.contasdacasa.divida.application.exception.DividaNaoEncontradaException;
import com.contasdacasa.divida.application.port.DividaPort;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarDividaUseCase {

    private final DividaPort dividaPort;

    public BuscarDividaUseCase(DividaPort dividaPort) {
        this.dividaPort = dividaPort;
    }

    public Divida executar(UUID id) {
        return dividaPort.buscarPorId(id).orElseThrow(() -> new DividaNaoEncontradaException(id));
    }
}
