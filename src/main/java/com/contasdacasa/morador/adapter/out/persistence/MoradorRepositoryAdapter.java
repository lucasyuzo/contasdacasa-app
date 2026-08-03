package com.contasdacasa.morador.adapter.out.persistence;

import com.contasdacasa.morador.domain.Morador;
import com.contasdacasa.morador.domain.MoradorRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class MoradorRepositoryAdapter implements MoradorRepository {

  private final MoradorJpaRepository jpaRepository;

  MoradorRepositoryAdapter(MoradorJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Morador salvar(Morador morador) {
    jpaRepository.save(
        new MoradorJpaEntity(morador.getId(), morador.getNome(), morador.getCasaId()));
    return morador;
  }

  @Override
  public Optional<Morador> buscarPorId(UUID id) {
    return jpaRepository.findById(id).map(this::toDomain);
  }

  @Override
  public List<Morador> listarPorCasa(UUID casaId) {
    return jpaRepository.findByCasaId(casaId).stream().map(this::toDomain).toList();
  }

  @Override
  public void remover(UUID id) {
    jpaRepository.deleteById(id);
  }

  private Morador toDomain(MoradorJpaEntity entity) {
    return Morador.reconstituir(entity.getId(), entity.getNome(), entity.getCasaId());
  }
}
