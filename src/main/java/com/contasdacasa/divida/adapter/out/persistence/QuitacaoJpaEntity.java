package com.contasdacasa.divida.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "quitacao")
class QuitacaoJpaEntity {

    @Id private UUID id;

    @Column(name = "divida_id")
    private UUID dividaId;

    private LocalDate data;

    @Column(precision = 12, scale = 2)
    private BigDecimal valor;

    protected QuitacaoJpaEntity() {}

    QuitacaoJpaEntity(UUID id, UUID dividaId, LocalDate data, BigDecimal valor) {
        this.id = id;
        this.dividaId = dividaId;
        this.data = data;
        this.valor = valor;
    }

    UUID getId() {
        return id;
    }

    UUID getDividaId() {
        return dividaId;
    }

    LocalDate getData() {
        return data;
    }

    BigDecimal getValor() {
        return valor;
    }
}
