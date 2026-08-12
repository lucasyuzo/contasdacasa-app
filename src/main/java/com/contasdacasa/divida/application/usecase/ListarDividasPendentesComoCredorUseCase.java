package com.contasdacasa.divida.application.usecase;

import com.contasdacasa.divida.application.domain.Divida;
import com.contasdacasa.divida.application.port.DividaPort;
import com.contasdacasa.morador.application.domain.Morador;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarDividasPendentesComoCredorUseCase {

    private final DividaPort dividaPort;

    public ListarDividasPendentesComoCredorUseCase(DividaPort dividaPort) {
        this.dividaPort = dividaPort;
    }

    public List<Divida> executar(Morador morador) {
        return dividaPort.listarPorPagador(morador.getId()).stream()
                .filter(Divida::estaPendente)
                .toList();
    }
}
