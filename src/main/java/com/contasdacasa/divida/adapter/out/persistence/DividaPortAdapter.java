package com.contasdacasa.divida.adapter.out.persistence;

import com.contasdacasa.divida.application.domain.Divida;
import com.contasdacasa.divida.application.domain.Quitacao;
import com.contasdacasa.divida.application.port.DividaPort;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
class DividaPortAdapter implements DividaPort {

    private final DividaJpaRepository dividaJpaRepository;
    private final QuitacaoJpaRepository quitacaoJpaRepository;

    DividaPortAdapter(
            DividaJpaRepository dividaJpaRepository, QuitacaoJpaRepository quitacaoJpaRepository) {
        this.dividaJpaRepository = dividaJpaRepository;
        this.quitacaoJpaRepository = quitacaoJpaRepository;
    }

    @Override
    public void salvarTodas(List<Divida> dividas) {
        dividas.forEach(
                divida ->
                        dividaJpaRepository.save(
                                new DividaJpaEntity(
                                        divida.getId(),
                                        divida.getDespesaId(),
                                        divida.getParticipanteId(),
                                        divida.getPagadorId(),
                                        divida.getValor())));
    }

    @Override
    public List<Divida> listarPorDespesa(UUID despesaId) {
        return dividaJpaRepository.findByDespesaId(despesaId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Divida> listarPorParticipante(UUID participanteId) {
        return dividaJpaRepository.findByParticipanteId(participanteId).stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Divida> listarPorPagador(UUID pagadorId) {
        return dividaJpaRepository.findByPagadorId(pagadorId).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Divida> buscarPorId(UUID id) {
        return dividaJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional
    public Quitacao registrarQuitacao(Quitacao quitacao) {
        quitacaoJpaRepository.save(
                new QuitacaoJpaEntity(
                        quitacao.getId(), quitacao.getDividaId(), quitacao.getData(), quitacao.getValor()));
        return quitacao;
    }

    private Divida toDomain(DividaJpaEntity entity) {
        List<Quitacao> quitacoes =
                quitacaoJpaRepository.findByDividaId(entity.getId()).stream()
                        .map(
                                quitacaoEntity ->
                                        Quitacao.reconstituir(
                                                quitacaoEntity.getId(),
                                                quitacaoEntity.getDividaId(),
                                                quitacaoEntity.getData(),
                                                quitacaoEntity.getValor()))
                        .toList();
        return Divida.reconstituir(
                entity.getId(),
                entity.getDespesaId(),
                entity.getParticipanteId(),
                entity.getPagadorId(),
                entity.getValor(),
                quitacoes);
    }
}
