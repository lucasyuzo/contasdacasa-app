package com.contasdacasa.despesa.application.domain;

import com.contasdacasa.despesa.application.exception.ParticipanteSemRendaException;
import com.contasdacasa.despesa.application.exception.ParticipanteSemValorFixoException;
import com.contasdacasa.despesa.application.exception.SomaDosValoresFixosDivergeDoValorTotalException;
import com.contasdacasa.divida.application.domain.Divida;
import com.contasdacasa.shared.domain.TipoRateio;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    private final TipoRateio tipoRateio;

    private Despesa(
            UUID id,
            UUID casaId,
            BigDecimal valor,
            Natureza natureza,
            UUID pagadorId,
            List<UUID> participantesIds,
            LocalDate dataVencimento,
            List<Divida> dividas,
            TipoRateio tipoRateio) {
        this.id = id;
        this.casaId = casaId;
        this.valor = valor;
        this.natureza = natureza;
        this.pagadorId = pagadorId;
        this.participantesIds = participantesIds;
        this.dataVencimento = dataVencimento;
        this.dividas = dividas;
        this.tipoRateio = tipoRateio;
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
                id,
                casaId,
                valor,
                natureza,
                pagadorId,
                participantesIds,
                dataVencimento,
                dividas,
                TipoRateio.IGUAL);
    }

    public static Despesa cadastrarComRateioPorRenda(
            UUID casaId,
            BigDecimal valor,
            Natureza natureza,
            UUID pagadorId,
            List<UUID> participantesIds,
            Map<UUID, BigDecimal> rendasPorParticipante,
            LocalDate dataVencimento) {
        UUID id = UUID.randomUUID();
        validarRendas(participantesIds, rendasPorParticipante);
        List<Divida> dividas =
                ratearPorRenda(id, valor, pagadorId, participantesIds, rendasPorParticipante);
        return new Despesa(
                id,
                casaId,
                valor,
                natureza,
                pagadorId,
                participantesIds,
                dataVencimento,
                dividas,
                TipoRateio.POR_RENDA);
    }

    public static Despesa cadastrarComRateioValorFixo(
            UUID casaId,
            BigDecimal valor,
            Natureza natureza,
            UUID pagadorId,
            List<UUID> participantesIds,
            Map<UUID, BigDecimal> valoresFixosPorParticipante,
            LocalDate dataVencimento) {
        UUID id = UUID.randomUUID();
        validarValoresFixos(valor, participantesIds, valoresFixosPorParticipante);
        List<Divida> dividas =
                ratearValorFixo(
                        id, valor, pagadorId, participantesIds, valoresFixosPorParticipante);
        return new Despesa(
                id,
                casaId,
                valor,
                natureza,
                pagadorId,
                participantesIds,
                dataVencimento,
                dividas,
                TipoRateio.VALOR_FIXO);
    }

    public static Despesa reconstituir(
            UUID id,
            UUID casaId,
            BigDecimal valor,
            Natureza natureza,
            UUID pagadorId,
            List<UUID> participantesIds,
            LocalDate dataVencimento,
            List<Divida> dividas,
            TipoRateio tipoRateio) {
        return new Despesa(
                id,
                casaId,
                valor,
                natureza,
                pagadorId,
                participantesIds,
                dataVencimento,
                dividas,
                tipoRateio);
    }

    private static List<Divida> ratearIgual(
            UUID despesaId, BigDecimal valor, UUID pagadorId, List<UUID> participantesIds) {
        long totalCentavos = valor.movePointRight(2).longValueExact();
        long valorBaseCentavos = totalCentavos / participantesIds.size();

        long[] centavosPorParticipante = new long[participantesIds.size()];
        Arrays.fill(centavosPorParticipante, valorBaseCentavos);

        return distribuirRestoEGerarDividas(
                despesaId, pagadorId, participantesIds, totalCentavos, centavosPorParticipante);
    }

    private static void validarRendas(
            List<UUID> participantesIds, Map<UUID, BigDecimal> rendasPorParticipante) {
        for (UUID participanteId : participantesIds) {
            BigDecimal renda = rendasPorParticipante.get(participanteId);
            if (renda == null || renda.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ParticipanteSemRendaException(participanteId);
            }
        }
    }

    private static List<Divida> ratearPorRenda(
            UUID despesaId,
            BigDecimal valor,
            UUID pagadorId,
            List<UUID> participantesIds,
            Map<UUID, BigDecimal> rendasPorParticipante) {
        long totalCentavos = valor.movePointRight(2).longValueExact();
        BigDecimal totalRenda =
                participantesIds.stream()
                        .map(rendasPorParticipante::get)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        long[] centavosPorParticipante = new long[participantesIds.size()];
        for (int i = 0; i < participantesIds.size(); i++) {
            BigDecimal renda = rendasPorParticipante.get(participantesIds.get(i));
            centavosPorParticipante[i] =
                    BigDecimal.valueOf(totalCentavos)
                            .multiply(renda)
                            .divide(totalRenda, 0, RoundingMode.FLOOR)
                            .longValueExact();
        }

        return distribuirRestoEGerarDividas(
                despesaId, pagadorId, participantesIds, totalCentavos, centavosPorParticipante);
    }

    private static void validarValoresFixos(
            BigDecimal valor,
            List<UUID> participantesIds,
            Map<UUID, BigDecimal> valoresFixosPorParticipante) {
        BigDecimal soma = BigDecimal.ZERO;
        for (UUID participanteId : participantesIds) {
            BigDecimal valorFixo = valoresFixosPorParticipante.get(participanteId);
            if (valorFixo == null) {
                throw new ParticipanteSemValorFixoException(participanteId);
            }
            soma = soma.add(valorFixo);
        }
        if (soma.compareTo(valor) != 0) {
            throw new SomaDosValoresFixosDivergeDoValorTotalException(valor, soma);
        }
    }

    private static List<Divida> ratearValorFixo(
            UUID despesaId,
            BigDecimal valor,
            UUID pagadorId,
            List<UUID> participantesIds,
            Map<UUID, BigDecimal> valoresFixosPorParticipante) {
        long totalCentavos = valor.movePointRight(2).longValueExact();
        long[] centavosPorParticipante = new long[participantesIds.size()];
        for (int i = 0; i < participantesIds.size(); i++) {
            centavosPorParticipante[i] =
                    valoresFixosPorParticipante
                            .get(participantesIds.get(i))
                            .movePointRight(2)
                            .longValueExact();
        }

        return distribuirRestoEGerarDividas(
                despesaId, pagadorId, participantesIds, totalCentavos, centavosPorParticipante);
    }

    private static List<Divida> distribuirRestoEGerarDividas(
            UUID despesaId,
            UUID pagadorId,
            List<UUID> participantesIds,
            long totalCentavos,
            long[] centavosPorParticipante) {
        long somaCentavos = Arrays.stream(centavosPorParticipante).sum();
        centavosPorParticipante[0] += totalCentavos - somaCentavos;

        List<Divida> dividas = new ArrayList<>();
        for (int i = 0; i < participantesIds.size(); i++) {
            UUID participanteId = participantesIds.get(i);
            if (participanteId.equals(pagadorId)) {
                continue;
            }
            dividas.add(
                    Divida.gerar(
                            despesaId,
                            participanteId,
                            pagadorId,
                            centavosParaValor(centavosPorParticipante[i])));
        }
        return dividas;
    }

    private static BigDecimal centavosParaValor(long centavos) {
        return BigDecimal.valueOf(centavos, 2);
    }
}
