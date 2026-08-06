package com.contasdacasa.morador.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface MoradorJpaRepository extends JpaRepository<MoradorJpaEntity, UUID> {

  List<MoradorJpaEntity> findByCasaId(UUID casaId);

  boolean existsByUsuarioIdAndCasaId(UUID usuarioId, UUID casaId);
}
