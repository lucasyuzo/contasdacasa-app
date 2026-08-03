package com.contasdacasa.casa.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface CasaJpaRepository extends JpaRepository<CasaJpaEntity, UUID> {}
