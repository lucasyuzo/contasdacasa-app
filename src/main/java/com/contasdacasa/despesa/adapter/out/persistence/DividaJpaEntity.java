package com.contasdacasa.despesa.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "divida")
class DividaJpaEntity {

    @Id private UUID id;

    @Column(name = "despesa_id")
    private UUID despesaId;

    @Column(name = "participante_id")
    private UUID participanteId;

    @Column(name = "pagador_id")
    private UUID pagadorId;

    @Column(precision = 12, scale = 2)
    private BigDecimal valor;

    protected DividaJpaEntity() {}

    DividaJpaEntity(UUID id, UUID despesaId, UUID participanteId, UUID pagadorId, BigDecimal valor) {
        this.id = id;
        this.despesaId = despesaId;
        this.participanteId = participanteId;
        this.pagadorId = pagadorId;
        this.valor = valor;
    }

    UUID getId() {
        return id;
    }

    UUID getDespesaId() {
        return despesaId;
    }

    UUID getParticipanteId() {
        return participanteId;
    }

    UUID getPagadorId() {
        return pagadorId;
    }

    BigDecimal getValor() {
        return valor;
    }
}
