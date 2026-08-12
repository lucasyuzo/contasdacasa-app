package com.contasdacasa.despesa.adapter.out.persistence;

import com.contasdacasa.despesa.application.domain.Despesa;
import com.contasdacasa.despesa.application.port.DespesaPort;
import com.contasdacasa.divida.application.domain.Divida;
import com.contasdacasa.divida.application.port.DividaPort;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
class DespesaPortAdapter implements DespesaPort {

    private final DespesaJpaRepository despesaJpaRepository;
    private final DividaPort dividaPort;

    DespesaPortAdapter(DespesaJpaRepository despesaJpaRepository, DividaPort dividaPort) {
        this.despesaJpaRepository = despesaJpaRepository;
        this.dividaPort = dividaPort;
    }

    @Override
    @Transactional
    public Despesa salvar(Despesa despesa) {
        despesaJpaRepository.save(
                new DespesaJpaEntity(
                        despesa.getId(),
                        despesa.getCasaId(),
                        despesa.getValor(),
                        despesa.getNatureza(),
                        despesa.getPagadorId(),
                        despesa.getParticipantesIds(),
                        despesa.getDataVencimento(),
                        despesa.getTipoRateio()));
        dividaPort.salvarTodas(despesa.getDividas());
        return despesa;
    }

    @Override
    public Optional<Despesa> buscarPorId(UUID id) {
        return despesaJpaRepository.findById(id).map(this::toDomain);
    }

    private Despesa toDomain(DespesaJpaEntity entity) {
        List<UUID> participantesIds = entity.getParticipantesIds();
        List<Divida> dividas =
                dividaPort.listarPorDespesa(entity.getId()).stream()
                        .sorted(
                                Comparator.comparingInt(
                                        divida -> participantesIds.indexOf(divida.getParticipanteId())))
                        .toList();
        return Despesa.reconstituir(
                entity.getId(),
                entity.getCasaId(),
                entity.getValor(),
                entity.getNatureza(),
                entity.getPagadorId(),
                entity.getParticipantesIds(),
                entity.getDataVencimento(),
                dividas,
                entity.getTipoRateio());
    }
}
