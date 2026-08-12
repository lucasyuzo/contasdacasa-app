package com.contasdacasa.morador.application.usecase;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.port.MoradorPort;
import com.contasdacasa.usuario.application.domain.Usuario;

import org.springframework.stereotype.Service;

@Service
public class CriarMoradorUseCase {

    private final MoradorPort moradorPort;

    public CriarMoradorUseCase(MoradorPort moradorPort) {
        this.moradorPort = moradorPort;
    }

    public Morador executar(Casa casa, Usuario usuario, String nome) {
        return moradorPort.salvar(Morador.adicionar(casa.getId(), usuario.getId(), nome));
    }
}
