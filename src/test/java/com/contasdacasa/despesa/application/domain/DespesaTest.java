package com.contasdacasa.despesa.application.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.contasdacasa.divida.application.domain.Divida;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

class DespesaTest {

    @Test
    void rateioIgualDivideValorExatamenteEntreParticipantes() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante1 = UUID.randomUUID();
        UUID participante2 = UUID.randomUUID();

        Despesa despesa =
                Despesa.cadastrarComRateioIgual(
                        casaId,
                        new BigDecimal("10.00"),
                        Natureza.VARIAVEL,
                        pagadorId,
                        List.of(participante1, participante2),
                        LocalDate.of(2026, 8, 10));

        assertThat(despesa.getDividas())
                .extracting(Divida::getParticipanteId, Divida::getPagadorId, Divida::getValor)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(participante1, pagadorId, new BigDecimal("5.00")),
                        Tuple.tuple(participante2, pagadorId, new BigDecimal("5.00")));
    }

    @Test
    void diferencaDeCentavoFicaComOPrimeiroParticipantePorOrdemDeCadastro() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante1 = UUID.randomUUID();
        UUID participante2 = UUID.randomUUID();
        UUID participante3 = UUID.randomUUID();

        Despesa despesa =
                Despesa.cadastrarComRateioIgual(
                        casaId,
                        new BigDecimal("10.00"),
                        Natureza.VARIAVEL,
                        pagadorId,
                        List.of(participante1, participante2, participante3),
                        LocalDate.of(2026, 8, 10));

        assertThat(despesa.getDividas())
                .extracting(Divida::getParticipanteId, Divida::getPagadorId, Divida::getValor)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(participante1, pagadorId, new BigDecimal("3.34")),
                        Tuple.tuple(participante2, pagadorId, new BigDecimal("3.33")),
                        Tuple.tuple(participante3, pagadorId, new BigDecimal("3.33")));
    }

    @Test
    void naoGeraDividaDoPagadorParaSiMesmoQuandoElePropriaEhParticipante() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante = UUID.randomUUID();

        Despesa despesa =
                Despesa.cadastrarComRateioIgual(
                        casaId,
                        new BigDecimal("10.00"),
                        Natureza.VARIAVEL,
                        pagadorId,
                        List.of(pagadorId, participante),
                        LocalDate.of(2026, 8, 10));

        assertThat(despesa.getDividas())
                .extracting(Divida::getParticipanteId, Divida::getPagadorId, Divida::getValor)
                .containsExactly(Tuple.tuple(participante, pagadorId, new BigDecimal("5.00")));
    }

    @Test
    void
            quandoOPagadorEhOPrimeiroParticipanteEleAbsorveADiferencaDeCentavoSemGerarDividaParaSiMesmo() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante1 = UUID.randomUUID();
        UUID participante2 = UUID.randomUUID();

        Despesa despesa =
                Despesa.cadastrarComRateioIgual(
                        casaId,
                        new BigDecimal("10.00"),
                        Natureza.VARIAVEL,
                        pagadorId,
                        List.of(pagadorId, participante1, participante2),
                        LocalDate.of(2026, 8, 10));

        assertThat(despesa.getDividas())
                .extracting(Divida::getParticipanteId, Divida::getPagadorId, Divida::getValor)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(participante1, pagadorId, new BigDecimal("3.33")),
                        Tuple.tuple(participante2, pagadorId, new BigDecimal("3.33")));
    }
}
