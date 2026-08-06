package com.contasdacasa.morador.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface MoradorJpaRepository extends JpaRepository<MoradorJpaEntity, UUID> {

    List<MoradorJpaEntity> findByCasaId(UUID casaId);

    boolean existsByUsuarioIdAndCasaId(UUID usuarioId, UUID casaId);
}
