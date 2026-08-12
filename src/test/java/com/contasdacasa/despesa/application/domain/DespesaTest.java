package com.contasdacasa.despesa.application.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.contasdacasa.despesa.application.exception.ParticipanteSemRendaException;
import com.contasdacasa.despesa.application.exception.ParticipanteSemValorFixoException;
import com.contasdacasa.despesa.application.exception.SomaDosValoresFixosDivergeDoValorTotalException;
import com.contasdacasa.divida.application.domain.Divida;
import com.contasdacasa.shared.domain.TipoRateio;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Test
    void rateioPorRendaDivideValorProporcionalmenteARendaDosParticipantes() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante1 = UUID.randomUUID();
        UUID participante2 = UUID.randomUUID();
        Map<UUID, BigDecimal> rendas =
                Map.of(
                        pagadorId, new BigDecimal("1000"),
                        participante1, new BigDecimal("3000"),
                        participante2, new BigDecimal("1000"));

        Despesa despesa =
                Despesa.cadastrarComRateioPorRenda(
                        casaId,
                        new BigDecimal("100.00"),
                        Natureza.VARIAVEL,
                        pagadorId,
                        List.of(pagadorId, participante1, participante2),
                        rendas,
                        LocalDate.of(2026, 8, 10));

        assertThat(despesa.getTipoRateio()).isEqualTo(TipoRateio.POR_RENDA);
        assertThat(despesa.getDividas())
                .extracting(Divida::getParticipanteId, Divida::getPagadorId, Divida::getValor)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(participante1, pagadorId, new BigDecimal("60.00")),
                        Tuple.tuple(participante2, pagadorId, new BigDecimal("20.00")));
    }

    @Test
    void diferencaDeCentavoDoRateioPorRendaFicaComOPrimeiroParticipantePorOrdemDeCadastro() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante1 = UUID.randomUUID();
        UUID participante2 = UUID.randomUUID();
        UUID participante3 = UUID.randomUUID();
        Map<UUID, BigDecimal> rendas = new HashMap<>();
        rendas.put(participante1, new BigDecimal("1000"));
        rendas.put(participante2, new BigDecimal("2000"));
        rendas.put(participante3, new BigDecimal("3000"));

        Despesa despesa =
                Despesa.cadastrarComRateioPorRenda(
                        casaId,
                        new BigDecimal("10.00"),
                        Natureza.VARIAVEL,
                        pagadorId,
                        List.of(participante1, participante2, participante3),
                        rendas,
                        LocalDate.of(2026, 8, 10));

        assertThat(despesa.getDividas())
                .extracting(Divida::getParticipanteId, Divida::getPagadorId, Divida::getValor)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(participante1, pagadorId, new BigDecimal("1.67")),
                        Tuple.tuple(participante2, pagadorId, new BigDecimal("3.33")),
                        Tuple.tuple(participante3, pagadorId, new BigDecimal("5.00")));
    }

    @Test
    void naoGeraDividaDoPagadorParaSiMesmoNoRateioPorRendaQuandoElePropriaEhParticipante() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante = UUID.randomUUID();
        Map<UUID, BigDecimal> rendas =
                Map.of(pagadorId, new BigDecimal("100"), participante, new BigDecimal("100"));

        Despesa despesa =
                Despesa.cadastrarComRateioPorRenda(
                        casaId,
                        new BigDecimal("10.00"),
                        Natureza.VARIAVEL,
                        pagadorId,
                        List.of(pagadorId, participante),
                        rendas,
                        LocalDate.of(2026, 8, 10));

        assertThat(despesa.getDividas())
                .extracting(Divida::getParticipanteId, Divida::getPagadorId, Divida::getValor)
                .containsExactly(Tuple.tuple(participante, pagadorId, new BigDecimal("5.00")));
    }

    @Test
    void rejeitaRateioPorRendaQuandoParticipanteNaoTemRendaCadastrada() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante = UUID.randomUUID();
        Map<UUID, BigDecimal> rendas = Map.of(pagadorId, new BigDecimal("100"));

        assertThatThrownBy(
                        () ->
                                Despesa.cadastrarComRateioPorRenda(
                                        casaId,
                                        new BigDecimal("10.00"),
                                        Natureza.VARIAVEL,
                                        pagadorId,
                                        List.of(pagadorId, participante),
                                        rendas,
                                        LocalDate.of(2026, 8, 10)))
                .isInstanceOf(ParticipanteSemRendaException.class);
    }

    @Test
    void rejeitaRateioPorRendaQuandoParticipanteTemRendaZeroOuNegativa() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante = UUID.randomUUID();
        Map<UUID, BigDecimal> rendas =
                Map.of(pagadorId, new BigDecimal("100"), participante, BigDecimal.ZERO);

        assertThatThrownBy(
                        () ->
                                Despesa.cadastrarComRateioPorRenda(
                                        casaId,
                                        new BigDecimal("10.00"),
                                        Natureza.VARIAVEL,
                                        pagadorId,
                                        List.of(pagadorId, participante),
                                        rendas,
                                        LocalDate.of(2026, 8, 10)))
                .isInstanceOf(ParticipanteSemRendaException.class);
    }

    @Test
    void rateioValorFixoGeraDividasComOsValoresDefinidosPorParticipante() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante1 = UUID.randomUUID();
        UUID participante2 = UUID.randomUUID();
        Map<UUID, BigDecimal> valoresFixos =
                Map.of(
                        pagadorId, new BigDecimal("20.00"),
                        participante1, new BigDecimal("70.00"),
                        participante2, new BigDecimal("10.00"));

        Despesa despesa =
                Despesa.cadastrarComRateioValorFixo(
                        casaId,
                        new BigDecimal("100.00"),
                        Natureza.VARIAVEL,
                        pagadorId,
                        List.of(pagadorId, participante1, participante2),
                        valoresFixos,
                        LocalDate.of(2026, 8, 10));

        assertThat(despesa.getTipoRateio()).isEqualTo(TipoRateio.VALOR_FIXO);
        assertThat(despesa.getDividas())
                .extracting(Divida::getParticipanteId, Divida::getPagadorId, Divida::getValor)
                .containsExactlyInAnyOrder(
                        Tuple.tuple(participante1, pagadorId, new BigDecimal("70.00")),
                        Tuple.tuple(participante2, pagadorId, new BigDecimal("10.00")));
    }

    @Test
    void naoGeraDividaDoPagadorParaSiMesmoNoRateioValorFixoQuandoElePropriaEhParticipante() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante = UUID.randomUUID();
        Map<UUID, BigDecimal> valoresFixos =
                Map.of(pagadorId, new BigDecimal("4.00"), participante, new BigDecimal("6.00"));

        Despesa despesa =
                Despesa.cadastrarComRateioValorFixo(
                        casaId,
                        new BigDecimal("10.00"),
                        Natureza.VARIAVEL,
                        pagadorId,
                        List.of(pagadorId, participante),
                        valoresFixos,
                        LocalDate.of(2026, 8, 10));

        assertThat(despesa.getDividas())
                .extracting(Divida::getParticipanteId, Divida::getPagadorId, Divida::getValor)
                .containsExactly(Tuple.tuple(participante, pagadorId, new BigDecimal("6.00")));
    }

    @Test
    void rejeitaRateioValorFixoQuandoSomaDosValoresDivergeDoValorTotal() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante = UUID.randomUUID();
        Map<UUID, BigDecimal> valoresFixos =
                Map.of(pagadorId, new BigDecimal("4.00"), participante, new BigDecimal("5.00"));

        assertThatThrownBy(
                        () ->
                                Despesa.cadastrarComRateioValorFixo(
                                        casaId,
                                        new BigDecimal("10.00"),
                                        Natureza.VARIAVEL,
                                        pagadorId,
                                        List.of(pagadorId, participante),
                                        valoresFixos,
                                        LocalDate.of(2026, 8, 10)))
                .isInstanceOf(SomaDosValoresFixosDivergeDoValorTotalException.class);
    }

    @Test
    void rejeitaRateioValorFixoQuandoParticipanteNaoTemValorFixoDefinido() {
        UUID casaId = UUID.randomUUID();
        UUID pagadorId = UUID.randomUUID();
        UUID participante = UUID.randomUUID();
        Map<UUID, BigDecimal> valoresFixos = Map.of(pagadorId, new BigDecimal("10.00"));

        assertThatThrownBy(
                        () ->
                                Despesa.cadastrarComRateioValorFixo(
                                        casaId,
                                        new BigDecimal("10.00"),
                                        Natureza.VARIAVEL,
                                        pagadorId,
                                        List.of(pagadorId, participante),
                                        valoresFixos,
                                        LocalDate.of(2026, 8, 10)))
                .isInstanceOf(ParticipanteSemValorFixoException.class);
    }
}
