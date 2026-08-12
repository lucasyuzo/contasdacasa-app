package com.contasdacasa.despesa.application.usecase;

import com.contasdacasa.despesa.application.domain.Despesa;
import com.contasdacasa.despesa.application.exception.DespesaNaoEncontradaException;
import com.contasdacasa.despesa.application.port.DespesaPort;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarDespesaUseCase {

    private final DespesaPort despesaPort;

    public BuscarDespesaUseCase(DespesaPort despesaPort) {
        this.despesaPort = despesaPort;
    }

    public Despesa executar(UUID id) {
        return despesaPort.buscarPorId(id).orElseThrow(() -> new DespesaNaoEncontradaException(id));
    }
}
