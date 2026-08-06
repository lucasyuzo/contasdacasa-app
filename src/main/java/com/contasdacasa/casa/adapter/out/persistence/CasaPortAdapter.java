package com.contasdacasa.casa.adapter.out.persistence;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.casa.application.port.CasaPort;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class CasaPortAdapter implements CasaPort {

  private final CasaJpaRepository jpaRepository;

  CasaPortAdapter(CasaJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Casa salvar(Casa casa) {
    jpaRepository.save(new CasaJpaEntity(casa.getId(), casa.getNome()));
    return casa;
  }

  @Override
  public Optional<Casa> buscarPorId(UUID id) {
    return jpaRepository
        .findById(id)
        .map(entity -> Casa.reconstituir(entity.getId(), entity.getNome()));
  }
}
