package com.contasdacasa.divida.application.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.contasdacasa.divida.application.exception.QuitacaoExcedeSaldoDaDividaException;
import com.contasdacasa.divida.application.exception.QuitacaoValorInvalidoException;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

class DividaTest {

    @Test
    void saldoEhOValorTotalQuandoNaoHaQuitacoes() {
        Divida divida = dividaDeCem();

        assertThat(divida.getSaldo()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(divida.estaPendente()).isTrue();
    }

    @Test
    void quitacaoParcialDeixaSaldoResidualEContinuaPendente() {
        Divida divida = dividaDeCem();

        Quitacao quitacao = divida.registrarQuitacao(LocalDate.of(2026, 8, 10), new BigDecimal("40.00"));

        Divida dividaAtualizada = reconstituirCom(divida, List.of(quitacao));
        assertThat(dividaAtualizada.getSaldo()).isEqualByComparingTo(new BigDecimal("60.00"));
        assertThat(dividaAtualizada.estaPendente()).isTrue();
    }

    @Test
    void quitacaoTotalZeraOSaldoENaoFicaMaisPendente() {
        Divida divida = dividaDeCem();

        Quitacao quitacao = divida.registrarQuitacao(LocalDate.of(2026, 8, 10), new BigDecimal("100.00"));

        Divida dividaAtualizada = reconstituirCom(divida, List.of(quitacao));
        assertThat(dividaAtualizada.getSaldo()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(dividaAtualizada.estaPendente()).isFalse();
    }

    @Test
    void rejeitaQuitacaoComValorMaiorQueOSaldo() {
        Divida divida = dividaDeCem();

        assertThatThrownBy(
                        () ->
                                divida.registrarQuitacao(
                                        LocalDate.of(2026, 8, 10), new BigDecimal("100.01")))
                .isInstanceOf(QuitacaoExcedeSaldoDaDividaException.class);
    }

    @Test
    void rejeitaQuitacaoComValorZeroOuNegativo() {
        Divida divida = dividaDeCem();

        assertThatThrownBy(
                        () -> divida.registrarQuitacao(LocalDate.of(2026, 8, 10), BigDecimal.ZERO))
                .isInstanceOf(QuitacaoValorInvalidoException.class);
        assertThatThrownBy(
                        () ->
                                divida.registrarQuitacao(
                                        LocalDate.of(2026, 8, 10), new BigDecimal("-10.00")))
                .isInstanceOf(QuitacaoValorInvalidoException.class);
    }

    private Divida dividaDeCem() {
        return Divida.reconstituir(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new BigDecimal("100.00"),
                List.of());
    }

    private Divida reconstituirCom(Divida divida, List<Quitacao> quitacoes) {
        return Divida.reconstituir(
                divida.getId(),
                divida.getDespesaId(),
                divida.getParticipanteId(),
                divida.getPagadorId(),
                divida.getValor(),
                quitacoes);
    }
}
