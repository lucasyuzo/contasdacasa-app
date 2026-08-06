package com.contasdacasa.despesa.adapter.out.persistence;

import com.contasdacasa.despesa.application.domain.Despesa;
import com.contasdacasa.despesa.application.domain.Divida;
import com.contasdacasa.despesa.application.port.DespesaPort;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
class DespesaPortAdapter implements DespesaPort {

    private final DespesaJpaRepository despesaJpaRepository;
    private final DividaJpaRepository dividaJpaRepository;

    DespesaPortAdapter(
            DespesaJpaRepository despesaJpaRepository, DividaJpaRepository dividaJpaRepository) {
        this.despesaJpaRepository = despesaJpaRepository;
        this.dividaJpaRepository = dividaJpaRepository;
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
                        despesa.getDataVencimento()));
        despesa.getDividas()
                .forEach(
                        divida ->
                                dividaJpaRepository.save(
                                        new DividaJpaEntity(
                                                divida.getId(),
                                                divida.getDespesaId(),
                                                divida.getParticipanteId(),
                                                divida.getPagadorId(),
                                                divida.getValor())));
        return despesa;
    }

    @Override
    public Optional<Despesa> buscarPorId(UUID id) {
        return despesaJpaRepository.findById(id).map(this::toDomain);
    }

    private Despesa toDomain(DespesaJpaEntity entity) {
        List<UUID> participantesIds = entity.getParticipantesIds();
        List<Divida> dividas =
                dividaJpaRepository.findByDespesaId(entity.getId()).stream()
                        .map(
                                dividaEntity ->
                                        Divida.reconstituir(
                                                dividaEntity.getId(),
                                                dividaEntity.getDespesaId(),
                                                dividaEntity.getParticipanteId(),
                                                dividaEntity.getPagadorId(),
                                                dividaEntity.getValor()))
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
                dividas);
    }
}
