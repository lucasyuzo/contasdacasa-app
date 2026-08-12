package com.contasdacasa.casa.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface CasaJpaRepository extends JpaRepository<CasaJpaEntity, UUID> {}
