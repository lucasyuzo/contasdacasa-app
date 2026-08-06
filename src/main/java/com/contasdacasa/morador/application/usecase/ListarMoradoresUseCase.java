package com.contasdacasa.morador.application.usecase;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.port.MoradorPort;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarMoradoresUseCase {

    private final MoradorPort moradorPort;

    public ListarMoradoresUseCase(MoradorPort moradorPort) {
        this.moradorPort = moradorPort;
    }

    public List<Morador> executar(Casa casa) {
        return moradorPort.listarPorCasa(casa.getId());
    }
}
