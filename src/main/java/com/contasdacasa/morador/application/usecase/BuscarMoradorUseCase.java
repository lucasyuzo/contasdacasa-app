package com.contasdacasa.morador.application.usecase;

import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.exception.MoradorNaoEncontradoException;
import com.contasdacasa.morador.application.port.MoradorPort;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarMoradorUseCase {

    private final MoradorPort moradorPort;

    public BuscarMoradorUseCase(MoradorPort moradorPort) {
        this.moradorPort = moradorPort;
    }

    public Morador executar(UUID id) {
        return moradorPort.buscarPorId(id).orElseThrow(() -> new MoradorNaoEncontradoException(id));
    }
}
