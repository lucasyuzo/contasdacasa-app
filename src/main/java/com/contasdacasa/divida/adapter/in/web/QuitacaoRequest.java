package com.contasdacasa.divida.adapter.in.web;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

record QuitacaoRequest(
        @NotNull LocalDate data,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal valor) {}
