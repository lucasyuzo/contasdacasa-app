package com.contasdacasa.casa.adapter.out.persistence;

import com.contasdacasa.despesa.application.domain.TipoRateio;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "casa")
class CasaJpaEntity {

    @Id private UUID id;

    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoRateio rateioPadrao;

    protected CasaJpaEntity() {}

    CasaJpaEntity(UUID id, String nome, TipoRateio rateioPadrao) {
        this.id = id;
        this.nome = nome;
        this.rateioPadrao = rateioPadrao;
    }

    UUID getId() {
        return id;
    }

    String getNome() {
        return nome;
    }

    TipoRateio getRateioPadrao() {
        return rateioPadrao;
    }
}
