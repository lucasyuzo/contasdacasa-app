package com.contasdacasa.casa.adapter.in.web;

import com.contasdacasa.despesa.application.domain.TipoRateio;

import jakarta.validation.constraints.NotNull;

record RateioPadraoRequest(@NotNull TipoRateio tipoRateio) {}
