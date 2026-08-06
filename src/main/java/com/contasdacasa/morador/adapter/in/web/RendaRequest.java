package com.contasdacasa.morador.adapter.in.web;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

record RendaRequest(@NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal valor) {}
