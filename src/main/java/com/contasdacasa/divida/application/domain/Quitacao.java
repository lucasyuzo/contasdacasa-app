package com.contasdacasa.divida.application.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
public class Quitacao {

    private final UUID id;
    private final UUID dividaId;
    private final LocalDate data;
    private final BigDecimal valor;

    private Quitacao(UUID id, UUID dividaId, LocalDate data, BigDecimal valor) {
        this.id = id;
        this.dividaId = dividaId;
        this.data = data;
        this.valor = valor;
    }

    static Quitacao registrar(UUID dividaId, LocalDate data, BigDecimal valor) {
        return new Quitacao(UUID.randomUUID(), dividaId, data, valor);
    }

    public static Quitacao reconstituir(UUID id, UUID dividaId, LocalDate data, BigDecimal valor) {
        return new Quitacao(id, dividaId, data, valor);
    }
}
