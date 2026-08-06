package com.contasdacasa.despesa.adapter.out.persistence;

import com.contasdacasa.despesa.application.domain.Natureza;
import com.contasdacasa.despesa.application.domain.TipoRateio;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "despesa")
class DespesaJpaEntity {

    @Id private UUID id;

    @Column(name = "casa_id")
    private UUID casaId;

    @Column(precision = 12, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    private Natureza natureza;

    @Column(name = "pagador_id")
    private UUID pagadorId;

    @ElementCollection
    @CollectionTable(name = "despesa_participante", joinColumns = @JoinColumn(name = "despesa_id"))
    @OrderColumn(name = "ordem")
    @Column(name = "morador_id")
    private List<UUID> participantesIds;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_rateio")
    private TipoRateio tipoRateio;

    protected DespesaJpaEntity() {}

    DespesaJpaEntity(
            UUID id,
            UUID casaId,
            BigDecimal valor,
            Natureza natureza,
            UUID pagadorId,
            List<UUID> participantesIds,
            LocalDate dataVencimento,
            TipoRateio tipoRateio) {
        this.id = id;
        this.casaId = casaId;
        this.valor = valor;
        this.natureza = natureza;
        this.pagadorId = pagadorId;
        this.participantesIds = participantesIds;
        this.dataVencimento = dataVencimento;
        this.tipoRateio = tipoRateio;
    }

    UUID getId() {
        return id;
    }

    UUID getCasaId() {
        return casaId;
    }

    BigDecimal getValor() {
        return valor;
    }

    Natureza getNatureza() {
        return natureza;
    }

    UUID getPagadorId() {
        return pagadorId;
    }

    List<UUID> getParticipantesIds() {
        return participantesIds;
    }

    LocalDate getDataVencimento() {
        return dataVencimento;
    }

    TipoRateio getTipoRateio() {
        return tipoRateio;
    }
}
