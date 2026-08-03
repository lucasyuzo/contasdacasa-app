package com.contasdacasa.casa.adapter.out.persistence;

import com.contasdacasa.casa.domain.Casa;
import com.contasdacasa.casa.domain.CasaRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class CasaRepositoryAdapter implements CasaRepository {

  private final CasaJpaRepository jpaRepository;

  CasaRepositoryAdapter(CasaJpaRepository jpaRepository) {
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
