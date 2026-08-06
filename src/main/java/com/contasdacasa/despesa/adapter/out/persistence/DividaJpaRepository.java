package com.contasdacasa.despesa.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

interface DividaJpaRepository extends JpaRepository<DividaJpaEntity, UUID> {

    List<DividaJpaEntity> findByDespesaId(UUID despesaId);
}
