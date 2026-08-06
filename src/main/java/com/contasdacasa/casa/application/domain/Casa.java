package com.contasdacasa.casa.application.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Casa {

    private final UUID id;
    private final String nome;

    private Casa(UUID id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public static Casa criar(String nome) {
        return new Casa(UUID.randomUUID(), nome);
    }

    public static Casa reconstituir(UUID id, String nome) {
        return new Casa(id, nome);
    }
}
