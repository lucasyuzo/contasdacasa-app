package com.contasdacasa.divida.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface QuitacaoJpaRepository extends JpaRepository<QuitacaoJpaEntity, UUID> {

    List<QuitacaoJpaEntity> findByDividaId(UUID dividaId);
}
