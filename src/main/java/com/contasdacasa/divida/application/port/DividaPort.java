package com.contasdacasa.divida.application.port;

import com.contasdacasa.divida.application.domain.Divida;
import com.contasdacasa.divida.application.domain.Quitacao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DividaPort {

    void salvarTodas(List<Divida> dividas);

    List<Divida> listarPorDespesa(UUID despesaId);

    List<Divida> listarPorParticipante(UUID participanteId);

    List<Divida> listarPorPagador(UUID pagadorId);

    Optional<Divida> buscarPorId(UUID id);

    Quitacao registrarQuitacao(Quitacao quitacao);
}
