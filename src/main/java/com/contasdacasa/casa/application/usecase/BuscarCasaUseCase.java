package com.contasdacasa.casa.application.usecase;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.casa.application.exception.CasaNaoEncontradaException;
import com.contasdacasa.casa.application.port.CasaPort;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarCasaUseCase {

    private final CasaPort casaPort;

    public BuscarCasaUseCase(CasaPort casaPort) {
        this.casaPort = casaPort;
    }

    public Casa executar(UUID id) {
        return casaPort.buscarPorId(id).orElseThrow(() -> new CasaNaoEncontradaException(id));
    }
}
