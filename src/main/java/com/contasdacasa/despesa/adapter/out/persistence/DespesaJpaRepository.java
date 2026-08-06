package com.contasdacasa.despesa.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface DespesaJpaRepository extends JpaRepository<DespesaJpaEntity, UUID> {}
