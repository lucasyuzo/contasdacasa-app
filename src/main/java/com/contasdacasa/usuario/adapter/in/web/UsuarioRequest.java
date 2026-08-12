package com.contasdacasa.usuario.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRequest(@NotBlank String nome) {}
