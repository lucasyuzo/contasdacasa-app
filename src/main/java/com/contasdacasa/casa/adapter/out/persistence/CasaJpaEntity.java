package com.contasdacasa.casa.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "casa")
class CasaJpaEntity {

    @Id private UUID id;

    private String nome;

    protected CasaJpaEntity() {}

    CasaJpaEntity(UUID id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    UUID getId() {
        return id;
    }

    String getNome() {
        return nome;
    }
}
