package com.contasdacasa.morador.adapter.out.persistence;

import com.contasdacasa.morador.application.domain.Morador;
import com.contasdacasa.morador.application.exception.UsuarioJaEhMoradorDaCasaException;
import com.contasdacasa.morador.application.port.MoradorPort;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
class MoradorPortAdapter implements MoradorPort {

  private static final String UNIQUE_USUARIO_CASA = "uk_morador_usuario_casa";

  private final MoradorJpaRepository jpaRepository;

  MoradorPortAdapter(MoradorJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Morador salvar(Morador morador) {
    try {
      jpaRepository.save(
          new MoradorJpaEntity(
              morador.getId(), morador.getNome(), morador.getCasaId(), morador.getUsuarioId()));
    } catch (DataIntegrityViolationException e) {
      if (violaUniqueUsuarioCasa(e)) {
        throw new UsuarioJaEhMoradorDaCasaException(morador.getUsuarioId(), morador.getCasaId());
      }
      throw e;
    }
    return morador;
  }

  private boolean violaUniqueUsuarioCasa(DataIntegrityViolationException e) {
    return e.getCause() instanceof ConstraintViolationException cve
        && UNIQUE_USUARIO_CASA.equals(cve.getConstraintName());
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

  @Override
  public boolean existePorUsuarioECasa(UUID usuarioId, UUID casaId) {
    return jpaRepository.existsByUsuarioIdAndCasaId(usuarioId, casaId);
  }

  private Morador toDomain(MoradorJpaEntity entity) {
    return Morador.reconstituir(
        entity.getId(), entity.getNome(), entity.getCasaId(), entity.getUsuarioId());
  }
}
