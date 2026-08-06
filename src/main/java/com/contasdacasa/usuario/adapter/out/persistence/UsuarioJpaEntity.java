package com.contasdacasa.usuario.adapter.out.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "usuario")
class UsuarioJpaEntity {

    @Id private UUID id;

    private String nome;

    protected UsuarioJpaEntity() {}

    UsuarioJpaEntity(UUID id, String nome) {
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
