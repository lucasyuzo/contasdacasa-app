package com.contasdacasa.morador.application.port;

import com.contasdacasa.morador.application.domain.Morador;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MoradorPort {

    Morador salvar(Morador morador);

    Optional<Morador> buscarPorId(UUID id);

    List<Morador> listarPorCasa(UUID casaId);

    void remover(UUID id);

    boolean existePorUsuarioECasa(UUID usuarioId, UUID casaId);
}
