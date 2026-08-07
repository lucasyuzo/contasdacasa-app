package com.contasdacasa.despesa.application.usecase;

import com.contasdacasa.casa.application.domain.Casa;
import com.contasdacasa.despesa.application.domain.Despesa;
import com.contasdacasa.despesa.application.domain.Natureza;
import com.contasdacasa.despesa.application.domain.TipoRateio;
import com.contasdacasa.despesa.application.port.DespesaPort;
import com.contasdacasa.morador.application.domain.Morador;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            TipoRateio tipoRateioRequisitado,
            Morador pagador,
            List<Morador> participantes,
            Map<UUID, BigDecimal> valoresFixosPorParticipante,
            LocalDate dataVencimento) {
        TipoRateio tipoRateio =
                tipoRateioRequisitado != null ? tipoRateioRequisitado : casa.getRateioPadrao();
        List<UUID> participantesIds = participantes.stream().map(Morador::getId).toList();
        Despesa despesa =
                switch (tipoRateio) {
                    case IGUAL ->
                            Despesa.cadastrarComRateioIgual(
                                    casa.getId(),
                                    valor,
                                    natureza,
                                    pagador.getId(),
                                    participantesIds,
                                    dataVencimento);
                    case POR_RENDA ->
                            Despesa.cadastrarComRateioPorRenda(
                                    casa.getId(),
                                    valor,
                                    natureza,
                                    pagador.getId(),
                                    participantesIds,
                                    rendasPorParticipante(participantes),
                                    dataVencimento);
                    case VALOR_FIXO ->
                            Despesa.cadastrarComRateioValorFixo(
                                    casa.getId(),
                                    valor,
                                    natureza,
                                    pagador.getId(),
                                    participantesIds,
                                    valoresFixosPorParticipante != null
                                            ? valoresFixosPorParticipante
                                            : Map.of(),
                                    dataVencimento);
                };
        return despesaPort.salvar(despesa);
    }

    private Map<UUID, BigDecimal> rendasPorParticipante(List<Morador> participantes) {
        Map<UUID, BigDecimal> rendas = new HashMap<>();
        for (Morador participante : participantes) {
            rendas.put(participante.getId(), participante.getRenda());
        }
        return rendas;
    }
}
