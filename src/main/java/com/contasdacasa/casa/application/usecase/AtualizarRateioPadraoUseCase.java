package com.contasdacasa.casa.application.usecase;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.casa.application.port.CasaPort;
import com.contasdacasa.despesa.application.domain.TipoRateio;

import org.springframework.stereotype.Service;

@Service
public class AtualizarRateioPadraoUseCase {

    private final CasaPort casaPort;

    public AtualizarRateioPadraoUseCase(CasaPort casaPort) {
        this.casaPort = casaPort;
    }

    public Casa executar(Casa casa, TipoRateio rateioPadrao) {
        return casaPort.salvar(casa.alterarRateioPadrao(rateioPadrao));
    }
}
