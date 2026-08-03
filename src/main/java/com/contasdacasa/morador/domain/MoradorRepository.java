package com.contasdacasa.morador.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MoradorRepository {

  Morador salvar(Morador morador);

  Optional<Morador> buscarPorId(UUID id);

  List<Morador> listarPorCasa(UUID casaId);

  void remover(UUID id);
}
