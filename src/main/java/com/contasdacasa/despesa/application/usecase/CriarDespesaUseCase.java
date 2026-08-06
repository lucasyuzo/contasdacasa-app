package com.contasdacasa.despesa.application.usecase;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.despesa.application.domain.Despesa;
import com.contasdacasa.despesa.application.domain.Natureza;
import com.contasdacasa.despesa.application.port.DespesaPort;
import com.contasdacasa.morador.application.domain.Morador;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class CriarDespesaUseCase {

    private final DespesaPort despesaPort;

    public CriarDespesaUseCase(DespesaPort despesaPort) {
        this.despesaPort = despesaPort;
    }

    public Despesa executar(
            Casa casa,
            BigDecimal valor,
            Natureza natureza,
            Morador pagador,
            List<Morador> participantes,
            LocalDate dataVencimento) {
        List<UUID> participantesIds = participantes.stream().map(Morador::getId).toList();
        Despesa despesa =
                Despesa.cadastrarComRateioIgual(
                        casa.getId(),
                        valor,
                        natureza,
                        pagador.getId(),
                        participantesIds,
                        dataVencimento);
        return despesaPort.salvar(despesa);
    }
}
