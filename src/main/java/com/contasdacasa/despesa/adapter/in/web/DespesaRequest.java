package com.contasdacasa.despesa.adapter.in.web;

import com.contasdacasa.despesa.application.domain.Natureza;
import com.contasdacasa.despesa.application.domain.TipoRateio;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

record DespesaRequest(
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal valor,
        @NotNull Natureza natureza,
        TipoRateio tipoRateio,
        @NotNull UUID pagadorId,
        @NotEmpty List<UUID> participantesIds,
        @NotNull LocalDate dataVencimento) {}
