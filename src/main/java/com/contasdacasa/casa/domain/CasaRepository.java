package com.contasdacasa.casa.domain;

import java.util.Optional;
import java.util.UUID;

public interface CasaRepository {

  Casa salvar(Casa casa);

  Optional<Casa> buscarPorId(UUID id);
}
