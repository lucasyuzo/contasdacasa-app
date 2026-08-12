package com.contasdacasa.morador.application.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public class Morador {

    private final UUID id;
    private final String nome;
    private final UUID casaId;
    private final UUID usuarioId;
    private final BigDecimal renda;

    private Morador(UUID id, String nome, UUID casaId, UUID usuarioId, BigDecimal renda) {
        this.id = id;
        this.nome = nome;
        this.casaId = casaId;
        this.usuarioId = usuarioId;
        this.renda = renda;
    }

    public static Morador adicionar(UUID casaId, UUID usuarioId, String nome) {
        return new Morador(UUID.randomUUID(), nome, casaId, usuarioId, null);
    }

    public static Morador reconstituir(
            UUID id, String nome, UUID casaId, UUID usuarioId, BigDecimal renda) {
        return new Morador(id, nome, casaId, usuarioId, renda);
    }

    public Morador atualizarRenda(BigDecimal renda) {
        return new Morador(id, nome, casaId, usuarioId, renda);
    }
}
