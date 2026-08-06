package com.contasdacasa.despesa.application.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Despesa {

    private final UUID id;
    private final UUID casaId;
    private final BigDecimal valor;
    private final Natureza natureza;
    private final UUID pagadorId;
    private final List<UUID> participantesIds;
    private final LocalDate dataVencimento;
    private final List<Divida> dividas;

    private Despesa(
            UUID id,
            UUID casaId,
            BigDecimal valor,
            Natureza natureza,
            UUID pagadorId,
            List<UUID> participantesIds,
            LocalDate dataVencimento,
            List<Divida> dividas) {
        this.id = id;
        this.casaId = casaId;
        this.valor = valor;
        this.natureza = natureza;
        this.pagadorId = pagadorId;
        this.participantesIds = participantesIds;
        this.dataVencimento = dataVencimento;
        this.dividas = dividas;
    }

    public static Despesa cadastrarComRateioIgual(
            UUID casaId,
            BigDecimal valor,
            Natureza natureza,
            UUID pagadorId,
            List<UUID> participantesIds,
            LocalDate dataVencimento) {
        UUID id = UUID.randomUUID();
        List<Divida> dividas = ratearIgual(id, valor, pagadorId, participantesIds);
        return new Despesa(
                id, casaId, valor, natureza, pagadorId, participantesIds, dataVencimento, dividas);
    }

    public static Despesa reconstituir(
            UUID id,
            UUID casaId,
            BigDecimal valor,
            Natureza natureza,
            UUID pagadorId,
            List<UUID> participantesIds,
            LocalDate dataVencimento,
            List<Divida> dividas) {
        return new Despesa(
                id, casaId, valor, natureza, pagadorId, participantesIds, dataVencimento, dividas);
    }

    private static List<Divida> ratearIgual(
            UUID despesaId, BigDecimal valor, UUID pagadorId, List<UUID> participantesIds) {
        long totalCentavos = valor.movePointRight(2).longValueExact();
        int quantidade = participantesIds.size();
        long valorBaseCentavos = totalCentavos / quantidade;
        long restoCentavos = totalCentavos % quantidade;

        List<Divida> dividas = new ArrayList<>();
        for (int i = 0; i < participantesIds.size(); i++) {
            UUID participanteId = participantesIds.get(i);
            if (participanteId.equals(pagadorId)) {
                continue;
            }
            long centavosParticipante = valorBaseCentavos + (i == 0 ? restoCentavos : 0);
            dividas.add(
                    Divida.gerar(
                            despesaId, participanteId, pagadorId, centavosParaValor(centavosParticipante)));
        }
        return dividas;
    }

    private static BigDecimal centavosParaValor(long centavos) {
        return BigDecimal.valueOf(centavos, 2);
    }
}
