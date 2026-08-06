package com.contasdacasa.casa.application.port;

import com.contasdacasa.casa.application.domain.Casa;

import java.util.Optional;
import java.util.UUID;

public interface CasaPort {

    Casa salvar(Casa casa);

    Optional<Casa> buscarPorId(UUID id);
}
