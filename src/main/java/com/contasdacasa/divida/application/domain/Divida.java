package com.contasdacasa.divida.application.domain;

import com.contasdacasa.divida.application.exception.QuitacaoExcedeSaldoDaDividaException;
import com.contasdacasa.divida.application.exception.QuitacaoValorInvalidoException;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
public class Divida {

    private final UUID id;
    private final UUID despesaId;
    private final UUID participanteId;
    private final UUID pagadorId;
    private final BigDecimal valor;
    private final List<Quitacao> quitacoes;

    private Divida(
            UUID id,
            UUID despesaId,
            UUID participanteId,
            UUID pagadorId,
            BigDecimal valor,
            List<Quitacao> quitacoes) {
        this.id = id;
        this.despesaId = despesaId;
        this.participanteId = participanteId;
        this.pagadorId = pagadorId;
        this.valor = valor;
        this.quitacoes = quitacoes;
    }

    public static Divida gerar(UUID despesaId, UUID participanteId, UUID pagadorId, BigDecimal valor) {
        return new Divida(UUID.randomUUID(), despesaId, participanteId, pagadorId, valor, List.of());
    }

    public static Divida reconstituir(
            UUID id,
            UUID despesaId,
            UUID participanteId,
            UUID pagadorId,
            BigDecimal valor,
            List<Quitacao> quitacoes) {
        return new Divida(id, despesaId, participanteId, pagadorId, valor, quitacoes);
    }

    public BigDecimal getSaldo() {
        BigDecimal totalQuitado =
                quitacoes.stream().map(Quitacao::getValor).reduce(BigDecimal.ZERO, BigDecimal::add);
        return valor.subtract(totalQuitado);
    }

    public boolean estaPendente() {
        return getSaldo().compareTo(BigDecimal.ZERO) > 0;
    }

    public Quitacao registrarQuitacao(LocalDate data, BigDecimal valorQuitacao) {
        if (valorQuitacao.compareTo(BigDecimal.ZERO) <= 0) {
            throw new QuitacaoValorInvalidoException();
        }
        if (valorQuitacao.compareTo(getSaldo()) > 0) {
            throw new QuitacaoExcedeSaldoDaDividaException(id);
        }
        return Quitacao.registrar(id, data, valorQuitacao);
    }
}
