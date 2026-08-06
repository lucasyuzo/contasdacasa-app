package com.contasdacasa.morador.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "morador")
class MoradorJpaEntity {

    @Id private UUID id;

    private String nome;

    @Column(name = "casa_id")
    private UUID casaId;

    @Column(name = "usuario_id")
    private UUID usuarioId;

    @Column(precision = 12, scale = 2)
    private BigDecimal renda;

    protected MoradorJpaEntity() {}

    MoradorJpaEntity(UUID id, String nome, UUID casaId, UUID usuarioId, BigDecimal renda) {
        this.id = id;
        this.nome = nome;
        this.casaId = casaId;
        this.usuarioId = usuarioId;
        this.renda = renda;
    }

    UUID getId() {
        return id;
    }

    String getNome() {
        return nome;
    }

    UUID getCasaId() {
        return casaId;
    }

    UUID getUsuarioId() {
        return usuarioId;
    }

    BigDecimal getRenda() {
        return renda;
    }
}
