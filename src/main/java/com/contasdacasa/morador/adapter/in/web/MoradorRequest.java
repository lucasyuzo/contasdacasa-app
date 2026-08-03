package com.contasdacasa.morador.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

record MoradorRequest(@NotBlank String nome) {}
