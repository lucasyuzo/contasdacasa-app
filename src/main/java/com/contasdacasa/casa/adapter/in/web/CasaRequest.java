package com.contasdacasa.casa.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

public record CasaRequest(@NotBlank String nome) {}
