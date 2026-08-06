package com.contasdacasa.morador.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

record MoradorRequest(@NotBlank String nome, @NotNull UUID usuarioId) {}
