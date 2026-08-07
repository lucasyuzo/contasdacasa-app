package com.contasdacasa.casa.application.domain;

import com.contasdacasa.despesa.application.domain.TipoRateio;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Casa {

    private final UUID id;
    private final String nome;
    private final TipoRateio rateioPadrao;

    private Casa(UUID id, String nome, TipoRateio rateioPadrao) {
        this.id = id;
        this.nome = nome;
        this.rateioPadrao = rateioPadrao;
    }

    public static Casa criar(String nome) {
        return new Casa(UUID.randomUUID(), nome, TipoRateio.IGUAL);
    }

    public static Casa reconstituir(UUID id, String nome, TipoRateio rateioPadrao) {
        return new Casa(id, nome, rateioPadrao);
    }

    public Casa alterarRateioPadrao(TipoRateio rateioPadrao) {
        return new Casa(id, nome, rateioPadrao);
    }
}
