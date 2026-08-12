package com.contasdacasa.despesa.application.port;

import com.contasdacasa.despesa.application.domain.Despesa;

import java.util.Optional;
import java.util.UUID;

public interface DespesaPort {

    Despesa salvar(Despesa despesa);

    Optional<Despesa> buscarPorId(UUID id);
}
